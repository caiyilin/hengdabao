var autoLayout1 = function(){
  if($("#home").height()){
    //$("#sections").css({"width":"100%","height":$("#home").height()});
  }

}

var FaAnimate = function(ele){
  if(ele.find(".sub_page").length>0){
    return;
  }else{}
  
  clearFaAnimate(ele);
  
  //console.dir(ele.find(".fa-ani"));
  
  ele.find(".fa-ani").each(function(){
    var item=$(this);
    item.addClass("animated");
    
    if(item.attr("fa-animate-delay")){
      setTimeout(function() {
      item.css("visibility","visible").addClass(item.attr("fa-animate-effect"));
      }, parseInt(item.attr("fa-animate-delay")));
    }else{
      item.css("visibility","visible").addClass(item.attr("fa-animate-effect"));
    }
    if(item.attr("fa-animate-duration")){//&& !item.attr("fa-animate-duration-cache")
      item.css("animation-duration",item.attr("fa-animate-duration"));
      item.attr("fa-animate-duration-cache","YES");
    }
  });
}

var clearFaAnimate = function(ele){
  ele.find(".fa-ani").each(function(){
    $(this).removeClass($(this).attr("fa-animate-effect")).removeClass("animated").css("visibility","hidden");
  });
}

var openSection = function(page){
  $("section:visible").hide();
  
  _LastPage.push(page);
  
  var targetEle=$("#"+page);
  targetEle.show();
  
  FaAnimate(targetEle);
  
  if(targetEle.find("navitem.selected").length==0){
    targetEle.find("navitem").eq(0).click();
  }
}

var showPage = function(page){
  $("page:visible").hide();
  
  var targetEle=$("#"+page);
  targetEle.show();
  
  FaAnimate(targetEle);
}

var jumpto = function(url){
  if(!/^http/.test(url)){
    //url = 'web/'+url;
    //if(url.substring(0,1)=='/'){
    //  url = url.substring(1);
    //}
    url = document.baseURI + url;
  }
  window.location.href= url;
}

var _LastPage=[];

$(function(){  //todo: fix url
  var res_path = '';//'/domains/ns.gudidea.com/sites/01/';
  $("#sub_bottom_nav").on("mouseover",function(){
    //$(".sub_bottom_subicon").hide();
    //$(".sub_bottom_sub_tohome").show();
  });
  
  $("#sub_bottom_nav").on("mouseout",function(){
    //$(".sub_bottom_subicon").show();
    //$(".sub_bottom_sub_tohome").hide();
  });
  
  $("#nav_to_home_btn").on("mouseover",function(){
    $(".fa-ani-nav-btn-rotating").removeClass("fa-ani-nav-btn-rotating")
    $(this).addClass("fa-ani-nav-btn-rotating");
  });
  
  $("#nav_to_home_btn").on("mouseout",function(){
    $(this).removeClass("fa-ani-nav-btn-rotating");
  });
  
  $("#nav_to_home_btn").on("click",function(){
    if($("#sub_bottom_nav_list:visible").length>0){
      $("#sub_bottom_nav_list").hide();
      $("#nav_mask").hide();
      $("#nav_text").attr("src", res_path + "assets/images/naigation1.png");
      $('.slip.slip'+current_category).show();
    }else{
      $("#sub_bottom_nav_list").show();
      $("#nav_mask").show();
      $("#nav_text").attr("src", res_path + "assets/images/naigation2.png");
      $('.slip.slip'+current_category).hide();
    }
  });
  
  $(".backbtn").click(function(){
    if(~document.referrer.indexOf('/info/query')
       || ~document.referrer.indexOf('/main')){
        window.history.go(-1);
    }
  })
  
  $("#nav_mask,#mask2").click(function(){
    $("#sub_bottom_nav_list").hide();
    $("#nav_mask").hide();
    $('.slip.slip'+current_category).show();
  })
  
  $(".sub_bottom_sub_tohome").click(function(){
    window.history.go(-1);
  });
    
  //$(".radius_btn").arctext({radius: 300});
  
  $(".nav-btn-outsilde .inner-text").arctext({radius: 80});
  
  $(".nav-btn-insilde .inner-text").arctext({radius: 52});

  $("*[openSection]").click(function(){
    var section=$(this).attr("openSection");
    
    openSection(section);
    
    if($(this)[0].tagName.toLowerCase()=="tabitem"){
      $("tabitem.selected").removeClass("selected");
      $(this).addClass("selected");
    }
  });
  
  $("*[showPage]").click(function(){
    var subPage=$(this).attr("showPage");
    
    showPage(subPage);

    if($(this)[0].tagName.toLowerCase()=="navitem"){
      $("navitem.selected").removeClass("selected");
      $(this).addClass("selected");
    }
  });
  
  $(".sub_bottom_subicon").click(function(){
    jumpto('/main');
  });
  
  $("back").click(function(){
    openSection(_LastPage[_LastPage.length-2]);
  });
  
  /*sth for test */
  $("#findjob .item").click(function(){
    openSection("findjob_detail");
  });
  
  /*init*/
  openSection("home");
  
  autoLayout1();
});