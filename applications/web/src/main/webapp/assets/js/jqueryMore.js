/**
 * 调用方式:$.M.funtion(params);
 */
(function($) {
	$.now_version = "1.0";
	$.support.cors = true;
	$.extend({M : {
		openAjax : true,
		isAndroid : false,
		isIos : false,
		isPad : false,
		last_click_time : 0,
		click_time : 0,
		clientType : "other",
		//测试环境
		baseUrl : 'http://',

		REGX_HTML_ENCODE : /"|&|'|<|>|[\x00-\x20]|[\x7F-\xFF]|[\u0100-\u2700]/g,
		REGX_HTML_DECODE : /&\w{1,};|&#\d{1,};/g,
		REGX_ENTITY_NUM : /\d{1,}/,
		REGX_TRIM : /(^\s*)|(\s*$)/g,
		isInDD : true,//是否在钉钉中
		HTML_DECODE : {
			"&lt;" : "<",
			"&gt;" : ">",
			"&amp;" : "&",
			"&nbsp;" : " ",
			"&quot;" : "\"",
			"&copy;" : "©"
		},
		language : function() {// 0表示中文，1表示英文
			var val = "0";
			if (window.localStorage.getItem('language') == undefined) {
			} else {
				val = window.localStorage.getItem('language');
			}
			return val;
		},
		encodeHtml : function(s) {// 加码html字符
			s = (s != undefined) ? s : this;
			return (typeof s != "string") ? s : s.replace(this.REGX_HTML_ENCODE, function($0) {
				var c = $0.charCodeAt(0), r = [ "&#" ];
				c = (c == 0x20) ? 0xA0 : c;
				r.push(c);
				r.push(";");
				return r.join("");
			});
		},
		decodeHtml : function(s) {// 解码html字符
			var HTML_DECODE = this.HTML_DECODE, REGX_NUM = this.REGX_ENTITY_NUM;
			s = (s != undefined) ? s : this;
			return (typeof s != "string") ? s : s.replace(this.REGX_HTML_DECODE, function($0) {
				var c = HTML_DECODE[$0];
				if (c == undefined) {
					var m = $0.match(REGX_NUM);
					if (m) {
						var cc = m[0];
						cc = (cc == 160) ? 32 : cc;
						c = String.fromCharCode(cc);
					} else {
						c = $0;
					}
				}
				return c;
			});
		},
		trim : function(s) {// 过滤空格
			s = (s != undefined) ? s : this;
			return (typeof s != "string") ? s : s.replace(this.REGX_TRIM, "");
		},
		hashCode : function() {
			var hash = this.__hash__, _char;
			if (hash == undefined || hash == 0) {
				hash = 0;
				for ( var i = 0, len = this.length; i < len; i++) {
					_char = this.charCodeAt(i);
					hash = 31 * hash + _char;
					hash = hash & hash; // Convert to 32bit integer
				}
				hash = hash & 0x7fffffff;
			}
			this.__hash__ = hash;
			return this.__hash__;
		},
		createXml : function(str) {// xml字符串转对象
			var xml = null;
			try {
				xml = new DOMParser().parseFromString(str,
						"text/xml");
			} catch (e) {
				xml = new ActiveXObject("Microsoft.XMLDOM");
				xml.async = false;
				xml.loadXML(str);
			}
			return xml;
		},
		rootVal : function(xmlStr) {// 获取xml根元素的Text
			var xml = this.createXml(xmlStr);
			if (xml.hasChildNodes()) {
				item = xml.childNodes.item(0);
				return $(item).text().replace(/\\/g, "\\\\");
			}
			return "";
		},
		rootJson : function(xmlStr) {// 获取xml根元素的Text
			var startIndex = xmlStr.indexOf('{');
			if (startIndex == -1) {
				return null;
			} else {
				var jsonStr = xmlStr.substring(startIndex);
				jsonStr = jsonStr.replace("</string>", "");
				var jsonObj = eval("(" + jsonStr + ")");
				return jsonObj;
			}
		},
		bigRootVal : function(xmlStr) {// 获取xml根元素的Text
			var jsonStr = xmlStr.substring(xmlStr.indexOf('{'));
			jsonStr = jsonStr.replace("</string>", "");
			var jsonObj = eval("(" + jsonStr + ")");
			return jsonObj;
		},
		bigRootOrg : function(xmlStr) {// 获取xml根元素的Text
			var jsonStr = xmlStr.substring(xmlStr.indexOf('['));
			jsonStr = jsonStr.replace("</string>", "");
			var jsonObj = eval("(" + jsonStr + ")");
			return jsonObj;
		},
		xmlToJson : function(xml) {// xml转化成json
			var obj = {};
			if (xml.nodeType == 1) { // element
				if (xml.attributes.length > 0) {
					obj["@attributes"] = {};
					for ( var j = 0; j < xml.attributes.length; j++) {
						var attribute = xml.attributes.item(j);
						obj["@attributes"][attribute.nodeName] = attribute.nodeValue;
					}
				}
			} else if (xml.nodeType == 3) { // text
				obj = xml.nodeValue;
			}
			if (xml.hasChildNodes()) {
				for ( var i = 0; i < xml.childNodes.length; i++) {
					var item = xml.childNodes.item(i);
					var nodeName = item.nodeName;
					if (typeof (obj[nodeName]) == "undefined") {
						nodeName == "#text" ? obj = this.xmlToJson(item) : obj[nodeName] = this.xmlToJson(item);
					} else {
						if (typeof (obj[nodeName].length) == "undefined") {
							var old = obj[nodeName];
							obj[nodeName] = [];
							obj[nodeName].push(old);
						}
						obj[nodeName].push(this.xmlToJson(item));
					}
				}
			}
			return obj;
		},
		get : function(options) {
			if($.M.isAndroid == true && !$.M.isOnLine()){
				options.error(XMLHttpRequest, null, null);
			} else if ($.M.isAndroid == false && $.M.isIos == true && $.M.bridge) {
				$.M.bridge.callHandler('isOnline', {}, function(response) {
					if (response == "1") {
						$.M.getOnLine(options);
					} else {
						options.error(XMLHttpRequest, null, null);
					}
				});
			} else {
				$.M.getOnLine(options);
			}
		},
		getOnLine : function(options) {
			options = $.extend({
				method : '',
				url : '',
				params : {},
				async:true,
				dataType : 'text',
				success : function(data) {

				},
				error : function(XMLHttpRequest, textStatus,
						errorThrown) {

				}
			}, options);
			var that = this;
			$.ajax({
				url : that.baseUrl + options.method,
				type : 'get',
				dataType : options.dataType,
				async: options.async,
				data : $.extend({
					'dateTime' : new Date().getTime(),
					'fromLocation': document.getElementById('pageLocation').innerHTML,
				}, options.params),
				success : function(data) {
					try {
						data = eval('(' + data + ')');
						options.success(data);
					} catch (e) {
					}
				},
				error : function(XMLHttpRequest, textStatus,
						errorThrown) {
					if (options.isTip == undefined || options.isTip == null || options.isTip != false) {
					}
					options.error(XMLHttpRequest, textStatus,
							errorThrown);
				}
			});
		},
		post : function(options, serverType) {// 封装的ajax post 方法
			options = $.extend({
				url : '',
				params : {},
				dataType : 'text',
				success : function(data) {
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
				}
			}, options);
			var serverUrl = "";
			var postData = null;
			if (serverType == "0") {
				serverUrl = $.M.baseUrl + options.method;
				postData = $.extend({
					'dateTime' : new Date().getTime(),
					'fromLocation': document.getElementById('pageLocation').innerHTML,
				}, options.params);
			} else if (serverType == "1") {
				serverUrl = $.M.noteUrl + options.method;
				postData = $.extend({
					'dateTime' : new Date().getTime(),
					'fromLocation': document.getElementById('pageLocation').innerHTML,
				}, options.params);
			}
			$.ajax({
				url : serverUrl,
				type : 'post',
				dataType : options.dataType,
				data : postData,
				success : function(data) {
					try {
						data = eval('(' + data + ')');
						options.success(data);
					} catch (e) {
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					if (options.isTip == undefined || options.isTip == null || options.isTip != false) {
						//alert("请确认网络连接,或联系管理员！");
					}
					options.error(XMLHttpRequest, textStatus, errorThrown);
				},
				complete: function () {
					typeof options.complete != 'undefined' && options.complete();
				}
			});
		},
		setCookie : function(name, value) {
			var Days = 30;
			var exp = new Date();
			exp.setTime(exp.getTime() + Days*24*60*60*1000);
			document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString(); 
		},
		getCookie : function(name) {
			var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
			if(arr=document.cookie.match(reg)) return unescape(arr[2]);
			else return null; 
		},
		delCookie : function(name) {
			var exp = new Date();
			exp.setTime(exp.getTime() - 1);
			var cval=$.M.getCookie(name);
			if(cval!=null) document.cookie= name + "="+cval+";expires="+exp.toGMTString();
		}
	}
	});
})($);

var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "//hm.baidu.com/hm.js?568cdd27e5db8ca3a8171506695debb5";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();

$(document).bind("pageshow", function(e) {
	var id = e.target.id;
	if ("goods_detail_page" != id && "activity_details_page" != id && "news_details_page" != id) {
		//$.M.getConfig(id, "", "");
	}
});

(function() {
	
	$.M.isPad = false;
	var jUserAgent = navigator.userAgent.toLowerCase();
	if (jUserAgent.indexOf("firefox") == -1) {
		$.M.isInDD = false;
	}
	var jIsIphoneOs = jUserAgent.match(/iphone os/i) == "iphone os", jIsIpad = jUserAgent
			.match(/ipad/i) == "ipad", jIsAndroid = jUserAgent
			.match(/android/i) == "android";
	if (jIsAndroid) {
		$.M.isAndroid = true;
		$.M.isIos = false;
		$.M.clientType = "android";
		$.M.last_click_time = new Date().getTime();
		document.addEventListener('click', function(e) {
			$.M.click_time = e['timeStamp'];
			if ($.M.click_time && ($.M.click_time - $.M.last_click_time) < 500) {
				try{
					e.stopImmediatePropagation();
				}catch(e){
				}
				try{
					e.preventDefault();
				}catch(e){
				}
				return false;
			}
			$.M.last_click_time = $.M.click_time;
		}, true);
	} else if (jIsIphoneOs || jIsIpad) {
		$.M.isAndroid = false;
		$.M.isIos = true;
		$.M.clientType = "ios";
		if(jIsIpad){
			$.M.isPad = true;
		}
	} else {
		$.M.isAndroid = false;
		$.M.isIos = false;
		$.M.clientType = "other";
	}
})();