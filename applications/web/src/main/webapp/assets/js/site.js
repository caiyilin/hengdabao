// 看起来像是工具脚本，源自/domains/ns.gudidea.com/sites/01/site.js
/* get url param ----- begin */
var get_url_param = function(key, is_top){
  var is_top = (is_top);
  var result = null;
  var url = (is_top) ? top.location.href : location.href;
  if(url.indexOf('?') == -1){
    result = null;;
  }else{
    if(url.indexOf('#') != -1){
      url = url.split('#')[0];
    }
    var params = url.split('?')[1].split('&');
    for(var i = 0;i < params.length;i++){
      if(params[i].indexOf(key) == 0 && params[i].indexOf('=') != -1){
        result = params[i].split('=')[1];
      }else{};
    };
  };
  return result;
};
/* get url param ----- end */

var refresh_list_andrew = function(page){
  API({'func' : 'content.list.andrew', 'page' : page, 'size' : 8});
};

var refresh_detail_andrew = function(id){
  API({'func' : 'content.detail.andrew', 'id' : id});
};

var API = function(attr){
  $.getJSON('/', attr, function(data){
    switch(attr.func){
      case 'content.list.andrew':
        var str_html = [];
        data = data.data;
        for(var i = 0; i < attr.size; i++){
          if(data.content[i]){
            var u = data.content[i];
            str_html.push('<li>');
            str_html.push('<a href="?page=andrew.detail&id=' + u.id + '">');
            str_html.push('<span class="lesson_title">' + u.name + '</span>');
            str_html.push('<br/>');
            str_html.push('<span class="lesson_summary">');
            str_html.push((u.description.length > 30) ? u.description.substr(0, 30) + '...' : u.description);
            str_html.push('</span>');
            str_html.push('</a>');
            str_html.push('</li>')
          }else{
            str_html.push('<li>');
            str_html.push('<span class="lesson_title">&nbsp;</span>');
            str_html.push('<br/>');
            str_html.push('<span class="lesson_summary">&nbsp;</span>');
            str_html.push('</li>')
          }
        }
        console.log(str_html);
        $('#content_andrew_list').html(str_html.join(''));
        
        var page_min = Math.floor(attr.page / 10) * 10 + 1;
        var page_max = page_min + 10;
        
        str_html = [];
        if(attr.page == 1){
          str_html.push('<span class="am-disabled pre">|<</span>');
          str_html.push('<span class="am-disabled pre"><</span>');
        }else{
          str_html.push('<span class="pre" onclick="javascript: refresh_list_andrew(1);">|<</span>');
          str_html.push('<span class="pre" onclick="javascript: refresh_list_andrew(' + (attr.page - 1) + ');"><</span>');
        }
        
        for(var i = page_min; i < page_max; i++){
          var str_status = '';
          if(attr.page == i){
            str_status = ' class="number am-active"';
          }else{
            if(i <= data.total_page){
            }else{
              str_status = ' class="number am-disabled"';
            }
          }
          str_html.push('<span' + str_status + ' onclick="' + ((str_status) ? '#' : 'javascript: refresh_list_andrew(' + i + ');') + '">' + i + '</span>');
        }
        if(attr.page == data.total_page){
          str_html.push('<span class="am-disabled next">></span>');
          str_html.push('<span class="am-disabled next">>|</span>');
        }else{
          str_html.push('<span class="next" href="javascript: refresh_list_andrew(' + (attr.page + 1) + ');">></span>');
          str_html.push('<span class="next" href="javascript: refresh_list_andrew(' + data.total_page + ');">>|</span>');
        }
        $('#pagination_andrew_list').html(str_html.join(''));
        break;
      case 'content.detail.andrew':
        data = data.data;
        $('#content_name_andrew').html(data.name);
        $('#content_time_create_andrew').html(data.time_create);
        $('#content_description_andrew').html(data.description);
        break;
      default:
        break;
    }
  });
};