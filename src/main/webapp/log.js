/*************************************************************************************/
function tes(paramName){   
    var reg = new RegExp("(^|/?|&)"+ paramName+"=([^&]*)(/s|&|$)", "i");
    if (reg.test(brccount.src)) //test为script ID 
        return RegExp.$2; 
    else
        return ""; 
}
var site=tes("id"); 
/*************************************************************************************/
var tu = encodeURIComponent(window.location.href);
/*************************************************************************************/
var t1 = Date.parse(new Date());
var i = 0;
var num = 0;
window.setInterval(function (){
	if(i>0){
		i=0;
		num++;
	}
},3000);
document.onmousemove=function(even){
	i++;
};
/*************************************************************************************/
var mousescrollNum =0;
var agent = navigator.userAgent;
if (/.*Firefox.*/.test(agent)) {
	document.addEventListener("DOMMouseScroll", function(e) {
		e = e || window.event;
		var detail = e.detail;
		if (detail = 0) {
			mousescrollNum ;
			//alert("scrollNum: "+scrollNum); 
		} else {
			mousescrollNum ++ ;
			//alert("scrollNum: "+scrollNum); 
		}
	});
}else {
	document.onmousewheel = function(e) {
		e = e || window.event;
		var wheelDelta = e.wheelDelta;
		if (wheelDelta = 0) {
			mousescrollNum ;
			//alert("scrollNum: "+scrollNum); 
		} else {
			mousescrollNum ++ ;
			//alert("scrollNum: "+scrollNum); 
		}
	};
}
/*************************************************************************************/
var n =0;
document.onclick = function(){
	n++;
};
/*************************************************************************************/

//var cookie=document.cookie.length;	
var expires= new Date();
	expires.setFullYear(expires.getFullYear()+50, expires.getMonth(), expires.getDate());
var ss = document.cookie.indexOf("u=");
if( ss == -1){
	var s = [];
	var hexDigits = "0123456789abcdef";
		for (var i = 0; i < 36; i++) {
			s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
		}
		s[14] = "4"; 
		s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  
		s[8] = s[13] = s[18] = s[23] = "-";	 
		
	var u = s.join("");
	document.cookie = "u="+u+";expires="+expires;
}
var ss2 = document.cookie.indexOf("c=");
if( ss2 == -1){
	one();
}
var x =getcookie("u").split("=")[1];
function one() {
		var httprequest = null;
		// 初始化XMLHttpRequest对象
		if (window.XMLHttpRequest) {
			// Firefox等现代浏览器中的XMLHttpRequest对象创建
			httprequest = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			// IE中的XMLHttpRequest对象创建
			httprequest = new ActiveXObject("Microsoft.XMLHTTP");
		}
		if (!httprequest) {
			alert("创建httprequest对象出现异常！");
		}
		httprequest.open("get","http://192.168.0.2:8080/q?u="+x+"&s="+site+"&p="+tu+"&t1="+t1,true); 
		httprequest.onreadystatechange = function () {
			if (httprequest.readyState == 4) {
				if (httprequest.status == 200) {
				   var c = httprequest.responseText;
				   if(c == null){
					   c = null;
				   }
				   document.cookie = "c="+c+";expires="+expires;
				}
				else {
					alert("AJAX服务器返回错误！");
				}
			}
		};
		httprequest.send();
};
function getcookie(name){
	var arr= document.cookie.indexOf(name+"=");
	if( arr == -1 ){
		return null;
		arr += name.length+1;
	}else{
		var end = document.cookie.indexOf(";",arr);
		if( end == -1){
			return unescape(document.cookie.substring(arr));
		}
		return unescape(document.cookie.substring(arr,end));;
	}
};



var v =getcookie("c").split("=")[1];

/*************************************************************************************/
var referrer=document.referrer;	
/*************************************************************************************/

var img=document.createElement("img");
var t=null;
var first=new Date();
window.onbeforeunload=function () {
	var last=new Date();
	t=last-first;
	if (t>0){
		t=t;
	}
	two();
	console.log();
};

/*************************************************************************************/
function two() {
	var httprequest = null;
	if (window.XMLHttpRequest) {
		httprequest = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		httprequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	if (!httprequest) {
		alert("创建httprequest对象出现异常！");
	}
	httprequest.open("get", "http://192.168.0.2:8080/l?u="+x+"&s="+site+"&c="+v+"&n="+n+"&t="+t+"&p="+tu+"&t1="+t1+"", false);
	httprequest.onreadystatechange = function () {
		if (httprequest.readyState == 4) {
			if (httprequest.status == 200) {
				httprequest.responseText;
			}
			else {
				alert("AJAX服务器返回错误！");
			}
		}
	};
	httprequest.send();
}

