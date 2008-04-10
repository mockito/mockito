
// the prototype-oriented programming library
function object(o) {
  function F() {}
  F.prototype = o;
  return new F();
}
// create a new object by using the given prototype then copying over
// all properties from v.
function derive(proto,v) {
  o = object(proto);
  for(p in v)
    o[p] = v[p];
  return o;
}

// extension to built-in objects
Array.prototype.forEach = function(closure) {
  for(var i=0; i<this.length; i++)
    closure(this[i]);
}

Array.prototype.apply = function(closure) {
  var r = [];
  for(var i=0; i<this.length; i++)
    r.push(closure(this[i]));
  return r;
}

// convert arguments set to array
function toArray(args) {
  var r = [];
  for(var i=0; i<args.length;i++)
    r.push(args[i]);
  return r;
}

String.prototype.startsWith = function(rhs) {
  var len = rhs.length;
  if(this.length<len)  return false;
  return this.substr(0,len)==rhs;
}

String.prototype.after = function(key) {
  var idx = this.lastIndexOf(key);
  if(idx>=0)
    return this.substring(idx+key.length);
  else
    return this;
}

String.prototype.contains = function(s) {
  return this.indexOf(s)>=0;
}

// return a function that counts up.
function iotaGen() {
  var i=0;
  return function() {
    return i++;
  }
}
var idGen = iotaGen(); // generate unique IDs for the system use


if(YAHOO.widget.MenuItem) {
  YAHOO.widget.MenuItem.prototype.addIcon = function(url) {
    // the following kind of works, but it doesn't align with the menu text nicely
    this.element.style.backgroundImage="url('"+url+"')";
    this.element.style.backgroundRepeat="no-repeat";
    this.element.style.paddingLeft="20px";
    this.element.style.backgroundPosition="left center";

    //var a = this.element.firstChild;
    //var img = document.createElement("img");
    //img.setAttribute("src",url);
    //a.insertBefore(img,a.firstChild);
  };
}


// class to fade a tag
function Fader(e,r,g,b) {
  self = this;
  this.grad = 0;
  this.steps = 20;

  this.trigger = function() {
    window.setTimeout(this.away,25);
  }

  this.set = function() {
    function ratio(i) {
      return (255*self.grad+i*(self.steps-self.grad))/self.steps;
    }
    e.style.backgroundColor = "rgb("+ratio(r)+","+ratio(g)+","+ratio(b)+")";
  }

  this.away = function() {
    self.set();
    if(++self.grad <= self.steps) {
      self.trigger();
    }
  }

  this.set();
  window.setTimeout(this.away,1000);
}


// manages loading of scripts and avoids duplicates.
var scriptLoadManager = {
  completedLoads : {},
  pendingLoads : {},

//
// overridable methods
//
  // compute href to .js from the key name.
  getScriptName : function(key) {return key;},

  // perform the post processing of the model objects
  postProcess : function(key,model) {},

  /*
    loads the given script.

    when the loading is complete, "callback(model)" is invoked
    where 'model' is what's given to 'loadComplete'.
  */
  load : function(key,callback) {
    var model = this.completedLoads[key];
    if(model!=null) {
      callback(model);
    } else {
      var inject=false;
      var p = this.pendingLoads[key];
      if(p==null) {
        p = [];
        inject = true;
        this.pendingLoads[key] = p;
      }
      p.push(callback);
      if(inject) {
        loadScript(this.getScriptName(key));
      }
    }
  },

  // to be invoked from scripts that are loaded.
  loadComplete : function(key,model) {
    this.postProcess(key,model);

    this.completedLoads[key] = model;

    var p = this.pendingLoads[key];
    this.pendingLoads[key]=null;

    // fire callbacks
    if(p!=null) {// this should be always true, but just to be defensive
      for( var i=0; i<p.length; i++ )
        p[i](model);
    }
  }
}


// take a base factory implementation and make it 'full' by deriving additional properties and functions.
function makeFull(f) {
  f = object(f);

  f.a  = f.reservedWord('abstract');
  f.as = f.reservedWord('assert');
  f.b  = f.primitiveType('boolean');
  f.br = f.reservedWord('break');
  f.by = f.primitiveType('byte');
  f.ca = f.reservedWord('case');
  f.ct = f.reservedWord('catch');
  f.ch = f.primitiveType('char');
  f.c  = f.reservedWord('class');
  f.cs = f.reservedWord('const');
  f.co = f.reservedWord('continue');
  f.de = f.reservedWord('default');
  f.d  = f.reservedWord('do');
  f.db = f.primitiveType('double');
  f.e  = f.reservedWord('else');
  f.en = f.reservedWord('enum');
  f.ex = f.reservedWord('extends');
  f.f  = f.reservedWord('final');
  f.fn = f.reservedWord('finally');
  f.fl = f.primitiveType('float');
  f.fo = f.reservedWord('for');
  f.i  = f.reservedWord('if');
  f.im = f.reservedWord('implements');
  f.ip = f.reservedWord('import');
  f.is = f.reservedWord('instanceof');
  f.j  = f.primitiveType('int');
  f.it = f.reservedWord('interface');
  f.l  = f.primitiveType('long');
  f.na = f.reservedWord('native');
  f.n  = f.reservedWord('new');
  f.pa = f.reservedWord('package');
  f.pi = f.reservedWord('private');
  f.po = f.reservedWord('protected');
  f.pu = f.reservedWord('public');
  f.r  = f.reservedWord('return');
  f.sh = f.primitiveType('short');
  f.s  = f.reservedWord('static');
  f.sf = f.reservedWord('strictfp');
  f.su = f.reservedWord('super');
  f.sw = f.reservedWord('switch');
  f.sy = f.reservedWord('synchronized');
  f.t  = f.reservedWord('this');
  f.tw = f.reservedWord('throw');
  f.ts = f.reservedWord('throws');
  f.tt = f.reservedWord('transient');
  f.tr = f.reservedWord('try');
  f.v  = f.primitiveType('void');
  f.vl = f.reservedWord('volatile');
  f.wh = f.reservedWord('while');
  f._  = f.w(1);

  f.conv = function(a) {
    if(typeof a=='string')
      a=f.sourceText(a);
    return a;
  };

  f.$ = function(/*...*/) {
    // convert one instance

    var len=arguments.length;
    if(len==1)  return f.conv(arguments[0]);

    return f.$$(arguments);
  };

  f.$$ = function(array) {
    // pack multiple items into a container
    var len=array.length;
    for( var i=len-1; i>=0; i-- )
      array[i] = f.conv(array[i]);
    return f.group(array);
  };

  f.group = function(children) {
    return function(context) {
      for(var i=0;i<children.length;i++)
        children[i](context);
    }
  };

  return f;
}

var makeWhitespace = function() {
  var s="";
  var wsTable = [];
  for(var i=0; i<8; i++ ) {
    wsTable[i]=s;
    s += ' ';
  }
  return function(n) {
    if(n<8)   return wsTable[n];

    var s="";
    while(n>=8) {
      s+="        ";
      n-=8;
    }
    return s+wsTable[n];
  }
}();

var tableEntry = {
  kind: null,   // tag that identifies the kind of table
  css:  null,   // CSS classes to be used for referencing this method
  href : null,  // link to the definition of this method
  usage : function(){},  // computes the "find usage" index key.
  displayText : function() {} // computes the text to be displayed
};

var typeTableEntry = derive(tableEntry,{
  kind: "type",
  binaryName: null, // encoded name as written in .js
  shortName: null,  // the last part of the class name identifier. "" if this is anonymous class
  linker: null,     // hyperlinks to this type shall be generated using this linker
  usage : function() {
    return this.binaryName+"#this";
  },
  displayText : function() {
    return this.shortName;
  },
  fullDisplayName : function() { // compute a human readable full name mainly for menus
    return this.binaryName.replace(/\$/g,".");
  },
  // return 'class', 'interface', 'annotation', or 'enum' depending on what this is
  getType : function() {
    if(this.css.contains("cl"))  return "class";
    if(this.css.contains("an"))  return "annotation";
    if(this.css.contains("en"))  return "enum";
    if(this.css.contains("it"))  return "interface";
    return undefined;
  }
});

// one entry of the method table
var methodTableEntry = derive(tableEntry,{
  kind: "method",
  usage : function() {
    return this.owner.binaryName+"#"+this.signature();
  },
  displayText : function() {
    return this.name;
  },

  owner: null, // reference to owner type
  name : null, // short method name
  params : [], // array of strings that represent erased parameter type names in FQCN

  // compute the method signature of the form "methodName(param1,param2,...)"
  signature : function() {
    var s = this.name+'(';
    for(var i=0; i<this.params.length; i++) {
      if(i!=0) s+=',';
      s+=this.params[i];
    }
    s+=')';
    return s;
  }
});

var localVariableTableEntry = derive(tableEntry,{
  kind: "localVariable",
  displayText: function() {
    return this.name;
  },
  css: "lv",
  usage: function() { // TODO: think about this more carefully.
    return this.href;
  },

  // constructor
  make : function(packedArray) {
    return derive(localVariableTableEntry,{name:packedArray[0], href:'#'+packedArray[1]});
  }
});
// currently variable decls/refs are not stored in *.js but we generate ones when we need them.
var fieldTableEntry = derive(tableEntry,{
  kind: "field",
  displayText: function() {
    return this.name;
  },
  css: "fi",
  owner: null,  // type table entry that owns this field
  usage: function() {
      return this.owner.binaryName+"#"+this.name;
  }
});

function makeFieldEntry(parentDecl, childASTs) {
  var f = object(fieldTableEntry);
  for( var i=0; i<childASTs.length; i++) {
    if(childASTs[i].identifier) {
      f.name = childASTs[i].identifier;
      break;
    }
  }
  var t = parentDecl; // find enclosing type table entry, to pick up the linker
  if(t.kind=="method")
    t=t.owner;
  f.owner = t;
  if(t.linker)
    f.href = t.linker.field(t,f.name);

  return f;
}

var abstractBuilder = {
  typeTable: function(/*...*/) {
    this.types = toArray(arguments).apply(function(packedArray) {
        var t = object(typeTableEntry);

        t.binaryName = packedArray[0];
        t.css = packedArray[1];

        idx=t.binaryName.lastIndexOf('.');
        if(idx<0)
          t.packageName="";
        else
          t.packageName=t.binaryName.substring(0,idx);

        t.shortName=t.binaryName.after(".").after("$");

        // TODO: it'd be nice if the source view page can be loaded on its own.
        // the way it's done today requires package view to be loaded.
        t.linker = window.top.packageView.main.linker.get(t.packageName);
        // YAHOO.log("linker for ["+t.packageName+"] is "+t.linker.name());

        t.href = t.linker.type(t);

        return t;
    });
  },
  methodTable: function(/*...*/) {
    var types=this.types;
    this.methods = toArray(arguments).apply(function(e) {
        var m = object(methodTableEntry);
        m.owner = types[e[0]];
        m.name = e[1];
        m.params = e[2];
        for(var j=0;j<m.params.length;j++) {
          if(typeof m.params[j] == "number")
            m.params[j] = types[m.params[j]].fullName;
        }

        m.css = e[3];
        m.href = m.owner.linker.method(m);
        // YAHOO.log("linker for ["+m.signature()+"] is "+m.owner.linker.name());

        return m;
    });
  },

  localVariableTable: function(/*...*/) {
    this.localVariables = toArray(arguments).apply(function(t) {
      return localVariableTableEntry.make(t);
    });
  },

  classDef : function(/*...*/) {
    return this.$$(arguments);
  }
};




//============================ bookmarks ============================
var bookmark = {}
bookmark.prototype = {
  id: null,      // unique ID assigned for each bookmark
  mark: "[]",    // bookmark char to be displayed. must be two chars wide
  caption: null, // menu caption
  items: [],     // array of menu item builders. Each is a function that returns menuItem.
  onclick: function(anchor) {
    if(bookmarkMenu!=null)
      bookmarkMenu.destroy();
    bookmarkMenu = new YAHOO.widget.Menu("bookmarkmenu");

    // Add caption
    var captionItem = new YAHOO.widget.MenuItem(this.caption);
    captionItem.cfg.setProperty("disabled",true);
    bookmarkMenu.addItem(captionItem);

    // Add items to the main menu
    this.items.forEach(function(item){bookmarkMenu.addItem(item())});

    bookmarkMenu.render(document.body);
    bookmarkMenu.cfg.setProperty("context", [anchor, "tl", "bl"]);
    bookmarkMenu.show();
  },
  buildAnchor: function() {
    //var a = document.createElement("a");
    //var text = document.createTextNode(this.mark);
    //a.appendChild(text);
    //var self = this;
    //a.onclick=function(){self.onclick(this);return false;}
    //a.setAttribute("href","#");
    //return a;
    return "<a href=# onclick='bookmarks["+this.id+"].onclick(this);return false;'>"+this.mark+"</a>"; 
  }
};

// create a bookmark for listing subtypes
bookmark.makeSubtype = function(typeTable,descendants) {
  var b = derive(bookmark.prototype,{
    mark: "\u25BC",
    caption: "Jump to subtypes"
  });

  b.id = idGen();
  b.items = [];
  descendants.forEach(function(d) {
    b.items.push(function() {
      var t = typeTable[d];
      var menuItem = new YAHOO.widget.MenuItem(t.fullDisplayName());
      menuItem.cfg.setProperty("url",t.href);
      menuItem.addIcon("resource-files/"+t.getType()+"_public.gif");
      return menuItem;
    })
  });

  return b;
}

bookmark.makeSuperMethods = function(methodTable,list) {
  return this.makeMethods("\u25B2","Jump to super method",methodTable,list);
}

bookmark.makeSubMethods = function(methodTable,list) {
  return this.makeMethods("\u25BC","Jump to overriding methods",methodTable,list);
}

// create a list of methods
bookmark.makeMethods = function(mark,caption,methodTable,list) {
  var b = derive(bookmark.prototype,{ mark:mark, caption:caption });

  b.id = idGen();
  b.items = [];
  list.forEach(function(idx) {
    b.items.push(function() {
      var m = methodTable[idx];
      var menuItem = new YAHOO.widget.MenuItem(m.owner.fullDisplayName());
      menuItem.cfg.setProperty("url",m.href);
      menuItem.addIcon("resource-files/"+m.owner.getType()+"_public.gif");
      return menuItem;
    })
  });

  return b;
}





//============================ lazy init actions ============================
// this code slowly executes additional actions 'in the background' by using timer.
// this is to reduce the amount of work needed to be done initially, which causes
// the lock up effect.

var lazyInitManager = {
  functions: [],  // code to be executed

  // adds a new lazy init script
  add: function(f) { this.functions.push(f); },

  start : function() {
    var idx=0; // next function to execute
    var f = this.functions;

    function step() {
      var limit=idx+10;
      var hardLimit = f.length
      if(limit>hardLimit) limit=hardLimit;

      for( ; idx<limit; idx++ )
        f[idx]();

      if(limit<hardLimit)
        window.setTimeout(step,10);
    }
    window.setTimeout(step,100);
  }
};
