
jQuery.beautyOfCode={settings:{autoLoad:true,baseUrl:'http://alexgorbatchev.com.s3.amazonaws.com/pub/sh/2.1.364/',scripts:'scripts/',styles:'styles/',theme:'Default',brushes:['Xml','JScript','CSharp','Plain'],config:{},defaults:{},ready:function(){jQuery.beautyOfCode.beautifyAll();}},init:function(settings){settings=jQuery.extend({},jQuery.beautyOfCode.settings,settings);if(!settings.config.clipboardSwf)
settings.config.clipboardSwf=settings.baseUrl+settings.scripts+'clipboard.swf';jQuery(document).ready(function(){if(!settings.autoLoad){settings.ready();}
else{jQuery.beautyOfCode.utils.loadCss(settings.baseUrl+settings.styles+'shCore.css');jQuery.beautyOfCode.utils.loadCss(settings.baseUrl+settings.styles+'shTheme'+settings.theme+'.css','shTheme');var scripts=new Array();scripts.push(settings.baseUrl+settings.scripts+'shCore.js');jQuery.each(settings.brushes,function(i,item){scripts.push(settings.baseUrl+settings.scripts+'shBrush'+item+".js");});jQuery.beautyOfCode.utils.loadAllScripts(scripts,function(){if(settings&&settings.config)
jQuery.extend(SyntaxHighlighter.config,settings.config);if(settings&&settings.defaults)
jQuery.extend(SyntaxHighlighter.defaults,settings.defaults);settings.ready();});}});},beautifyAll:function(){jQuery("pre.code:has(code[class]),code.code").beautifyCode();},utils:{loadScript:function(url,complete){jQuery.ajax({url:url,complete:function(){complete();},type:'GET',dataType:'script',cache:true});},loadAllScripts:function(urls,complete){if(!urls||urls.length==0)
{complete();return;}
var first=urls[0];jQuery.beautyOfCode.utils.loadScript(first,function(){jQuery.beautyOfCode.utils.loadAllScripts(urls.slice(1,urls.length),complete);});},loadCss:function(url,id){var headNode=jQuery("head")[0];if(url&&headNode)
{var styleNode=document.createElement('link');styleNode.setAttribute('rel','stylesheet');styleNode.setAttribute('href',url);if(id)styleNode.id=id;headNode.appendChild(styleNode);}},addCss:function(css,id){var headNode=jQuery("head")[0];if(css&&headNode)
{var styleNode=document.createElement('style');styleNode.setAttribute('type','text/css');if(id)styleNode.id=id;if(styleNode.styleSheet){styleNode.styleSheet.cssText=css;}
else{jQuery(styleNode).text(css);}
headNode.appendChild(styleNode);}},addCssForBrush:function(brush,highlighter){if(brush.isCssInitialized)
return;jQuery.beautyOfCode.utils.addCss(highlighter.Style);brush.isCssInitialized=true;},parseParams:function(params){var trimmed=jQuery.map(params,jQuery.trim);var paramObject={};var getOptionValue=function(name,list){var regex=new RegExp('^'+name+'\\[([^\\]]+)\\]$','gi');var matches=null;for(var i=0;i<list.length;i++)
if((matches=regex.exec(list[i]))!=null)
return matches[1];return null;}
var handleValue=function(flag){var flagValue=getOptionValue('boc-'+flag,trimmed);if(flagValue)paramObject[flag]=flagValue;};handleValue('class-name');handleValue('first-line');handleValue('tab-size');var highlight=getOptionValue('boc-highlight',trimmed);if(highlight)paramObject['highlight']=jQuery.map(highlight.split(','),jQuery.trim);var handleFlag=function(flag){if(jQuery.inArray('boc-'+flag,trimmed)!=-1)
paramObject[flag]=true;else if(jQuery.inArray('boc-no-'+flag,trimmed)!=-1)
paramObject[flag]=false;};handleFlag('smart-tabs');handleFlag('ruler');handleFlag('gutter');handleFlag('toolbar');handleFlag('collapse');handleFlag('auto-links');handleFlag('light');handleFlag('wrap-lines');handleFlag('html-script');return paramObject;}}};jQuery.fn.beautifyCode=function(brush,params){var saveBrush=brush;var saveParams=params;this.each(function(i,item){var $item=jQuery(item);var $code=$item.is('code')?$item:$item.children("code");var code=$code[0];var classItems=code.className.replace(/.+?(brush:|language-)/,'$1').replace('language-','').split(" ");var brush=saveBrush?saveBrush:classItems[0];var elementParams=jQuery.beautyOfCode.utils.parseParams(classItems);var params=jQuery.extend({},SyntaxHighlighter.defaults,saveParams,elementParams);if(params['html-script']=='true')
{highlighter=new SyntaxHighlighter.HtmlScript(brush);}
else
{var brush=SyntaxHighlighter.utils.findBrush(brush);if(brush)
highlighter=new brush();else
return;}
jQuery.beautyOfCode.utils.addCssForBrush(brush,highlighter);if($item.is("pre")&&($code=$item.children("code")))
$item.text($code.text());highlighter.highlight($item.html(),params);highlighter.source=item;$item.replaceWith(highlighter.div);});};