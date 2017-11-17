/** 日志服务器地址 **/
var logserver = "http://192.168.0.4:8080";
var adaPageInTime = 0;
var adaSiteId;
var adaClientId;
var adaChannelId="";
var adaMouseMoveTiems;
var adaMouseClickTimes;

/** JS页面加载后立即执行 **/
adaPageIn();
	
/** 检测关闭事件，在关闭前推送日志 **/
window.onbeforeunload = function () {
	console.log("开始推送日志");
	adaPutLog();
	console.log("结束推送日志");
};

window.setInterval(function (){
	console.log("开始推送日志");
	adaPutLog();
	console.log("结束推送日志");
},5000);//每3秒钟计算一次鼠标滑动次数,3秒内无聊滑动多少次记1次

window.onclose = function(){
	return false;
}

/** 打开页面处理 初始化  **/
function adaPageIn(){
	/** 记录进入页面时间  **/
	adaPageInTime = new Date();
	/** 获得站点ID **/
	adaSiteId = adaGetSiteId();
	
	/** 判断如果Cookie中未生成客户端ID,则生成新的客户端ID **/
	var existsClientId = (document.cookie.indexOf("u=") != -1);
	if(existsClientId){
		adaClientId = adaGetcookie("u").split("=")[1];
	}else {
		adaClientId = createUUID();
		/** 将客户端ID保存到Cookie中 **/
		document.cookie = "u="+adaClientId+";expires=100000000000000";
	}

	/** 判断渠道ID是否存在，如果不存在则查询渠道ID **/
	var existsChannelId = (document.cookie.indexOf("c=") != -1);
	if(existsChannelId){
		adaChannelId = adaGetcookie("c").split("=")[1];
	}else{
		adaQueryChannelId();
	}
}

function createUUID(){
	var s = [];
	var hexDigits = "0123456789abcdef";
		for (var i = 0; i < 36; i++) {
			s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
		}
		s[14] = "4"; 
		s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  
		s[8] = s[13] = s[18] = s[23] = "";	 
		
	var u = s.join("");
	return u;
}
/*************************************************************************************/
	
/** 获取站点ID **/
function adaGetSiteId(){   
    var reg = new RegExp("(^|/?|&)siteId=([^&]*)(/s|&|$)", "i");
    if (reg.test(adajs.src))
        return RegExp.$2; 
    else
        return ""; 
}

/** 鼠标滑动次数计数 **/
var adaTempMouseMoveTimes = 0;
window.setInterval(function (){
	if(adaTempMouseMoveTimes > 0 ){
		adaTempMouseTimes = 0;
		adaMouseMoveTiems++;
	}
},3000);//每3秒钟计算一次鼠标滑动次数,3秒内无聊滑动多少次记1次

document.onmousemove=function(even){
	adaTempMouseMoveTimes++;
};
/*************************************************************************************/

/** 鼠标点击次数计数 **/
var adaMouseClickTimes =0;
document.onclick = function(){
	adaMouseClickTimes++;
};
/*************************************************************************************/

/** 查询渠道ID并村粗COOKIE**/
function adaQueryChannelId() {
		var httprequest = null;
		if (window.XMLHttpRequest) {
			httprequest = new XMLHttpRequest();
		}else if (window.ActiveXObject) {
			httprequest = new ActiveXObject("Microsoft.XMLHTTP");
		}
		
		if (!httprequest) {
			console.log("初始化Httprequest失败");
		}
		var encodeURI = encodeURIComponent(window.location.href);
		httprequest.open("get",logserver+"/q?u="+adaClientId+"&s="+adaSiteId+"&p="+encodeURI+"&t1="+Date.parse(new Date()),true); 
		httprequest.onreadystatechange = function () {
			if (httprequest.readyState == 4) {
				if (httprequest.status == 200) {
				   var ret = httprequest.responseText;
				   console.log("查询渠道ID,ret->"+ret);
				   if(ret != null && ret!= "undefined" && ret != ""){
					   adaChannelId = ret;
					   document.cookie = "c="+adaChannelId+";expires=100000000000000";
				   }
				}else{
					console.log("查询渠道ID失败");
				}
			}
		};
		httprequest.send();
};
function adaGetcookie(name){
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

/*************************************************************************************/
/** 推送日志 **/
function adaPutLog() {
	var httprequest = null;
	if (window.XMLHttpRequest) {
		httprequest = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		httprequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	if (!httprequest) {
		console.log("初始化httprequest失败");
	}

	var now = new Date();
	var t = now - adaPageInTime;
	var p = encodeURIComponent(window.location.href);
	httprequest.open("get", logserver + "/l?u="+adaClientId+"&s="+adaSiteId+"&c="+adaChannelId+"&n="+adaMouseClickTimes+"&t="+t+"&p="+p+"&t1="+Date.parse(new Date()), false);
	httprequest.onreadystatechange = function () {
		if (httprequest.readyState == 4) {
			if (httprequest.status == 200) {
				httprequest.responseText;
			}
			else {
				console.log("推送日志请求错误，state"+httprequest.readyState);
			}
		}
	};
	httprequest.send();
}