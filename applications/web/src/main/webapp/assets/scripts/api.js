function API(attr){
	var target=null;
	if(attr.target){
		target=attr.target;
		attr.target="json";
	}
	
	if(attr.func!="user.login"){
		$(".spinner").show();
	}
	
	var label_type={
		"market":"大盘",
		"stock":"个股"
	};
	
	var label_type_tail={
		"market":"点",
		"stock":"元"
	};
	
	var label_dapan=["上证指数","深证成数","创业板指","沪深300","中小板指","上证180","上证50","上证380","恒生指数"];
	
	var label_description={
		"small_up":"小幅度上涨超",
		"big_up":"大幅度上涨超",
		"small_down":"小幅度下降超",
		"big_down":"大幅度下降超"
	};
	
	var level_creator=["未知","初学乍到","游学四方","有学而志","青年俊才",
		"初为人师","有智之士","智者达观","仁人明师","仁者无惑","仁者无敌",
		"尊者淡泊","大智若愚","圣心如水","归隐居士","千古圣人"];
	
	var quiz_status=["进行","结束","停止"];
	
	var selectedStr=["agree","disagree"];
	
	$.getJSON("/",attr,function(rz){

		//console.dir(rz);
		
		if(attr.func=="user.login"){
			_my_has_beans=1200;
			use_my_beans(0);
		}
		
		if(attr.func=="quiz.list.ongoing"){
			
			var content=rz.data.content;
			var html="";
			
			
			for(var i=0;i<content.length;i++){
				
				var name=content[i]["name"];//.replcae(/\s+/g,"");
				if(content[i]["label_type"]=="stock"){
					var nameArr=name.split("|");
					name=nameArr[0]+"("+nameArr[1]+")";
				}else{
					name=label_dapan[name];
				}
				
				content[i]["avatar_creator"]="tmpimg/1.png";
				content[i]["quiz_status"]=1;
				content[i]["level_creator"]=1;
				content[i]["time_finish"]=content[i]["time_finish"].substr(5,5);
				
				html+='<div class="ongoing_item" quiz_id="'+content[i]["id"]+'" style="'+
	    				'min-height:180px;'+
	                    'background-size:100% 100%;'+
	                    'background-image:url(images/list-block-bg2.png);'+
	                    'background-repeat:no-repeat;'+
	                    'background-position:center center;'+
	                    'padding:10px 20px; '+
	                    'margin:0 20px 20px 20px;">'+
	    	'<div>'+
	        	'<div class="left text-center" style="width:25%;">'+
	            	'<img src="'+content[i]["avatar_creator"]+'"  style="margin-top:-22px" class="avatar-s quiz_item_avatar" />'+
	                '<div class="people_name quiz_item_name">'+content[i]["name_creator"]+'</div>'+
	                '<div class="people_title quiz_item_level">L'+content[i]["level_creator"]+' '+level_creator[content[i]["level_creator"]]+'</div>'+
	            '</div>'+
	            '<div class="right quiz_item_title" style="width:65%;font-size:22px;line-height:140%;font-weight:bold">预测'+content[i]["time_finish"]+"收盘时"+label_type[content[i]["label_type"]]+''+name+'会'+label_description[content[i]["description"]]+''+content[i]["value"]+label_type_tail[content[i]["label_type"]]+'</div>'+
	            '<div class="clear"></div>'+
	        '</div>'+
	        '<div style="padding:16px 0;">'+
	        	'<div class="left text-center" style="width:40%;margin-left:5%">'+
	            	''+parseInt(content[i]["nop_agree"])+'人<br/>'+
	            	'<img src="images/btn-agreeit.png"  onclick="setBet(this,\'agree\',\''+content[i]["id"]+'\')" style="width:100%;height:auto" />'+
	            '</div>'+
	            '<div class="right text-center" style="width:40%;margin-right:5%">'+
	            	''+parseInt(content[i]["nop_disagree"])+'人<br/>'+
	            	'<img src="images/btn-disagree.png" onclick="setBet(this,\'disagree\',\''+content[i]["id"]+'\')" style="width:100%;height:auto" />'+
	            '</div>'+
	            '<div class="clear"></div>'+
	        '</div>'+
	    '</div>';
	  
			}
			

			target.find(".listContent").html(html);
			target.attr("loaded","LOADED");
			
		}
		
		if(attr.func=="quiz.agree"){
			alert("您支持该预测成功");
			use_my_beans(attr.score);
			$("#page_joinnow").find(".btnBack").click();
			setMyTools();
		}
		
		if(attr.func=="quiz.disagree"){
			alert("您不支持该预测成功");
			use_my_beans(attr.score);
			$("#page_joinnow").find(".btnBack").click();
			setMyTools();
		}
		
		if(attr.func=="quiz.create"){
			//alert("创建预测成功");
			//alert(1);
			use_my_beans(attr.score);
			showPageJoined();
			setMyTools();
		}
		
		//参与历史
		if(attr.func=="quiz.list.mine.join"){
			var content=rz.data.content;
			var html="";
			
			for(var i=0;i<content.length;i++){
				var name=content[i]["name"];//.replcae(/\s+/g,"");
				if(content[i]["label_type"]=="stock"){
					var nameArr=name.split("|");
					name=nameArr[0]+"("+nameArr[1]+")";
				}else{
					name=label_dapan[name];
				}
				
				content[i]["avatar_creator"]="tmpimg/1.png";
				content[i]["quiz_status"]=1;
				content[i]["level_creator"]=1;
				content[i]["time_finish"]=content[i]["time_finish"].substr(5,5);
				
				var resultStr='';
				if(content[i]["name_bet"]!=content[i]["quiz_result"]){
					resultStr='<span class="text-lost">你输了!损失'+parseInt(content[i]["score_result"])+'颗股神豆</span>';
				}else{
					resultStr='<span class="text-win">你赢了!获的'+parseInt(content[i]["score_result"])+'颗股神豆</span>';
				}
				
				content[i]["avatar_creator"]="tmpimg/1.png";
				content[i]["quiz_status"]=1;
				content[i]["level_creator"]=1;
				
				html+='<div quiz_id="'+content[i]["id"]+'" style="'+
    				'min-height:180px;'+
                    'background-size:100% 100%;'+
                    'background-image:url(images/list-block-bg3.png);'+
                    'background-repeat:no-repeat;'+
                    'background-position:center center;'+
                    'padding:0px 20px 3px 20px;'+
                    'margin:0 20px 20px 20px;">'+
			    	'<div>'+
			        	'<div class="left text-center" style="width:25%;">'+
			            	'<img src="'+content[i]["avatar_creator"]+'" style="margin-top:-12px" class="avatar-s" />'+
			                '<div class="people_name">'+content[i]["name_creator"]+'</div>'+
			                '<div class="people_title">L'+content[i]["level_creator"]+' '+level_creator[content[i]["level_creator"]]+'</div>'+
			            '</div>'+
			            '<div class="right" style="width:65%;font-size:20px;line-height:140%;padding-top:20px;">'+
			            	'['+quiz_status[parseInt(content[i]["quiz_status"])]+']预测'+content[i]["time_finish"]+"收盘时"+label_type[content[i]["label_type"]]+name+'会'+label_description[content[i]["description"]]+content[i]["value"]+label_type_tail[content[i]["label_type"]]+
			            '</div>'+ 
			            '<div class="clear"></div>'+
			        '</div>';
		        if(parseInt(content[i]["quiz_status"])>0){
			       html+='<div style="padding:16px 0;">'+
			        '	<div class="text-center" style="margin-bottom:12px;">'+
			            	'你的选择: <img src="images/btn-'+content[i]["name_bet"]+'it.png" style="width:60px;height:auto" />'+
			            '</div>'+
			            '<div class="text-center" style="margin-bottom:5px;">'+
			            	'投入'+parseInt(content[i]["score_mine"])+'股神豆<br/>'+resultStr+
			            '</div>'+ 
			        '</div>';
		    	}
		    	html+='</div>';
			}
			target.find(".listContent").html(html);
			target.attr("loaded",true);
		}
		
		if(attr.func=="quiz.list.mine.found"){
			var content=rz.data.content;
			var html="";
			
			for(var i=0;i<content.length;i++){
				var name=content[i]["name"];//.replcae(/\s+/g,"");
				if(content[i]["label_type"]=="stock"){
					var nameArr=name.split("|");
					name=nameArr[0]+"("+nameArr[1]+")";
				}else{
					name=label_dapan[name];
				}
				
				content[i]["avatar_creator"]="tmpimg/1.png";
				content[i]["quiz_status"]=1;
				content[i]["level_creator"]=1;
				content[i]["time_finish"]=content[i]["time_finish"].substr(5,5);
				
				var resultStr='';
				if(content[i]["name_bet"]!=content[i]["quiz_result"]){
					resultStr='<span class="text-lost">你输了!损失'+parseInt(content[i]["score_result"])+'颗股神豆</span>';
				}else{
					resultStr='<span class="text-win">你赢了!获的'+parseInt(content[i]["score_result"])+'颗股神豆</span>';
				}
				
				html+='<div quiz_id="'+content[i]["id"]+'" style="'+
    				'min-height:180px;'+
                    'background-size:100% 100%;'+
                    'background-image:url(images/list-block-bg3.png);'+
                    'background-repeat:no-repeat;'+
                    'background-position:center center;'+
                    'padding:0px 20px 3px 20px;'+
                    'margin:0 20px 20px 20px;">'+
			    	'<div>'+
			        	'<div class="left text-center" style="width:25%;">'+
			            	'<img src="'+content[i]["avatar_creator"]+'" style="margin-top:-12px" class="avatar-s" />'+
			                '<div class="people_name">'+content[i]["name_creator"]+'</div>'+
			                '<div class="people_title">L'+content[i]["level_creator"]+' '+level_creator[content[i]["level_creator"]]+'</div>'+
			            '</div>'+
			            '<div class="right" style="width:65%;font-size:20px;line-height:140%;padding-top:20px;">'+
			            	'['+quiz_status[parseInt(content[i]["quiz_status"])]+']我预测'+content[i]["time_finish"]+"收盘时"+label_type[content[i]["label_type"]]+name+'会'+label_description[content[i]["description"]]+content[i]["value"]+label_type_tail[content[i]["label_type"]]+
			            '</div>'+ 
			            '<div class="clear"></div>'+
			        '</div>';
		        if(parseInt(content[i]["quiz_status"])>0){
			       html+='<div style="padding:16px 0;">'+
			            '<div class="text-center" style="margin-bottom:5px;">'+
			            	'投入'+parseInt(content[i]["score_mine"])+'股神豆<br/>'+resultStr+
			            '</div>'+ 
			        '</div>';
		    	}
		    	html+='</div>';
			}
			target.find(".listContent").html(html);
			target.attr("loaded",true);
		}
		
		if(attr.func=="product.list"){
			var content=rz.data.content;
			var html="";
			
			for(var i=0;i<content.length;i++){
				
				content[i]["product_img"]="tmpimg/tmsd.png";
				
				html+='<div onclick="showBuy(this)" class="left text-center" product_id="'+content[i]["id"]+'" style="width:50%;'+
            	'background-size:100% 100%;'+
                'background-image:url(images/list-half-block-bg.png);'+
                'background-repeat:no-repeat;'+
                'background-position:center center;'+
                'min-height:180px;'+
            '">'+
                    '<div class="text-center" style="padding-top:10px;margin:0 20px;;font-size:13px;">'+
                        '<img src="'+content[i]["product_img"]+'" class="shop_item_product_img" style="width:50%;height:auto;margin:20px;" />'+
                        '<br /><span class="shop_item_name">'+content[i]["name"]+
                    '</span></div>'+
                    '<div class="text-center" style="padding:10px 0;font-size:18px;color:#FF0">'+
                        ' <img src="images/bean-xs.png" style="width:auto;height:20px;"> <span class="shop_item_price">'+content[i]["price"]+
                    '</span></div>'+           	
            '</div>';
			}
			target.find(".listContent").html(html);
			target.attr("loaded",true);
		}
		
		if(attr.func=="inventory.buy"){
			alert("购买成功");
			use_my_beans(attr.score);
			setMyTools();
			$("#page_shop_item").find(".btnBack").click();
		}
		
		if(attr.func=="inventory.list.mine"){
			var content=rz.data.content;
			var html="";
			
			for(var i=0;i<content.length;i++){
				
				content[i]["product_img"]="tmpimg/tmsd.png";
				
				html+='<div class="left text-center" style="width:50%;'+
            	'background-size:100% 100%;'+
                'background-image:url(images/list-half-block-bg.png);'+
                'background-repeat:no-repeat;'+
                'background-position:center center;'+
                'min-height:180px;'+
            '">'+
                    '<div class="text-center" style="padding-top:10px;margin:0 20px;;font-size:13px;">'+
                        '<img src="'+content[i]["product_img"]+'" style="width:40%;height:auto;margin:20px;" />'+
                        '<br />'+content[i]["name"]+
                    '</div>'+
                    '<div class="text-center" style="padding:10px 0;font-size:18px;color:#FF0">'+
                         '数量: '+content[i]["count"]+
                    '</div>'+           	
            '</div>';
			}
			target.find(".listContent").html(html);
			target.attr("loaded",true);
		}
		if(attr.func!="user.login"){
			$(".spinner").hide();
		}
		
		//console.dir(rz);
	});
}