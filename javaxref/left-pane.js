// add script tag to DOM to load it
function loadScript(href) {
    head = document.getElementsByTagName("head")[0];
    script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = href;
    head.appendChild(script);
}

// turn a span into a caption bar (that can also display a tip from buttons)
function Caption(e) {
  e.caption = e.innerHTML; // the initial caption is the current HTML.

  e.setCaption = function(caption) {
    this.caption = caption;
    if(!this.tipMode)
      this.innerHTML = caption;
  }
  e.showTip = function(tip) {
    this.innerHTML=tip;
    this.tipMode=true;
  }
  e.cancelTip = function() {
    this.tipMode=false;
    this.innerHTML=this.caption;
  }
}

// toggle button class for filter
//   e : the <img> element to be used as a button
//   callback : invoked when an image is clicked
function ToggleButton(e,callback) {
  e.isPressed = function() {
    return this.onclick==this.release;
  }
  e.postClick = function() {
    this.onmouseover(); // update the tooltip
    if(callback!=null)
      callback(this);
  }
  e.press = function() {
    this.className="button pressed";
    this.onclick=this.release;
    this.postClick();
  }
  e.release = function() {
    this.className="button";
    this.onclick=this.press;
    this.postClick();
  }
  e.onclick = e.press;
  e.onmouseover = function() {
    caption.showTip(this.getAttribute(this.isPressed()?"tip2":"tip1"));
  }
  e.onmouseout = function() {
    caption.cancelTip();
  }
}

// radio button where multiple buttons form a single logical group
// and only one in a group can be selected.
//
//  group : an array that will store all the members
function RadioButton(e,group,callback) {
  group.push(e);

  e.isPressed = function() {
    return this.className="button pressed"
  }
  e.onclick = function() {
    this.className="button pressed";
    for(var i=0; i<group.length; i++ ) {
      if(group[i]!=this)
        group[i].className="button";
    }
    if(callback!=null)
      callback(this);
  }
  e.onmouseover = function() {
    caption.showTip(this.getAttribute("tip"));
  }
  e.onmouseout = function() {
    caption.cancelTip();
  }
}

// ordinary button
function Button(e,callback) {
  e.onclick = function() {
    if(callback!=null)
      callback(this);
  }
  e.onmouseover = function() {
    caption.showTip(this.getAttribute("tip"));
  }
  e.onmouseout = function() {
    caption.cancelTip();
  }
}