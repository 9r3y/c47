if(typeof Object.create != 'function'){
    /**
     * 创建一个对象，它的__proto__指向参数o
     * @param o
     * @returns {Object.F}
     */
	Object.create = function(o){
		function F(){}
		F.prototype=o;
		return new F();
	};
}

/**
 * 生成guid  格式为“54E52592-313E-4F8B-869B-58D61F00DC74"
 * @returns {string}
 */
Math.guid=function(){
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c){
		var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
		return v.toString(16);
	}).toUpperCase();
};

/*********************** Class *************************/

/**
 * 创建一个构造方法，并具有类的特征
 * @param parent 返回的构造方法的prototype将是一个__proto__指向parent的prototype的空对象
 * @returns {Function}
 * @constructor
 */
function Class(parent){
	if(parent&&typeof(parent)!='function'){
		throw 'parent is not a constructor';
	}
	var klass=function(){
		this.init.apply(this,arguments);//构造对象时以新对象的执行环境调用init原型方法
	};
	
	//继承parent的原型属性
	if(parent){
		function F(){}
		F.prototype=parent.prototype;
		var prototype=new F();
		prototype.constructor=klass;
		klass.prototype=prototype;
        klass._super = klass.__proto__;
	}
	klass.fn=klass.prototype;//fn属性指向构造方法的原型
	klass.fn.init=function(){};
    /**
     * 返回一个匿名函数，它将以实例的执行环境调用参数指向的函数
     * @param fn
     * @returns {Function}
     */
	klass.fn.proxy=function(fn){
		var thes = this;
		return function(){
			return fn.apply(thes, arguments);
		};
	};
    klass.fn.parent = klass;//实例共享属性parent指向构造方法
	//if(parent)	klass._super=parent;//构造方法的_super属性指向父类构造方法
	//klass._super=klass.__proto__;

    /**
     * 扩展类构造函数的属性
     * @param obj
     */
	klass.extend=function(obj){
		for(var i in obj){
			klass[i]=obj[i];
		}
		var extended=obj.extended;
		if(extended)	extended(klass);
	};

    /**
     * 扩展类构造函数的原型，它能被所有类实例共享
     * @param obj
     */
	klass.include=function(obj){
		for(var i in obj){
			klass.fn[i]=obj[i];
		}
		var included=obj.included;
		if(included)	included(klass);
	};
	
	return klass;
}

/*********************** Model and Data *************************/

//模型对象，所有子模型对象的父对象
var Model={
    /**
     * 父模型被子模型继承时以父模型执行环境调用
     */
	inherited:function(){},
    /**
     * 创建子模型时以子模型的执行环境调用调用
     */
	created:function(){
		this.records={};//新模型的数据集
        this.attribute=[];//新模型的属性名数组
	},
	prototype:{//所有模型实例共有的原型对象
		init:function(){}
	},
    /**
     * 创建一个子模型
     */
	create:function(){
		var obj=Object.create(this);
		obj._super=this;//子模型的_super属性指向父模型
		obj.prototype=obj.fn=Object.create(this.prototype);
        obj.fn.parent = obj;//子模型实例共享parent属性，指向子模型自己
		obj.created();
		this.inherited(obj);
		return obj;
	},
    /**
     * 创建模型实例，实例的__proto__指向模型的prototype
     * @returns {*}
     */
	init:function(){
		var instance=Object.create(this.prototype);
		//instance.parent=this;//模型实例的parent属性指向模型
		instance.init.apply(this,arguments);
		return instance;	
	},
    /**
     * 扩展模型的属性
     * @param o
     */
	extend:function(o){
		jQuery.extend(this,o);
		var extended=o.extended;
		if(extended)	extended(this);
	},
    /**
     * 扩展模型的原型，即所有模型实例的共享对象
     * @param o
     */
	include:function(o){//
		jQuery.extend(this.prototype,o);
		var included=o.included;
		if(included)	included(this);
		
	}
};
//添加实例属性 
Model.include({
    /**
     * 初始化模型实例的属性
     * @param attr
     */
	init:function(attr){
		if(attr)	this.load(attr);
	},
	load:function(attr){
		for(var name in attr){
			this[name]=att[name];
		}
	},
	newRecord:true,
    /**
     * 将模型实例储存到模型数据集
     */
	create:function(){
		if(!this.id)	this.id = Math.guid();
		this.newRecord = false;
		this.parent.records[this.id]=this.dup();
	},
    /**
     * 将模型实例从模型数据集中删除
     */
	destroy:function(){
		delete this.parent.records[this.id];
	},
	update:function(){
		this.parent.records[this.id]=this.dup();
	},
    /**
     * 保存模型实例副本到模型数据集
     */
	save:function(){
		this.newRecord?this.create():this.update();
	},
    /**
     * 返回模型实例的副本
     * @returns {*}
     */
	dup:function(){
		return jQuery.extend(true,{},this);
	},
    /**
     * 将模型实例按照模型属性数组转化成简单对象并返回
     * @returns {{}}
     */
    attributes: function() {
        var result = {};
        for (var i in this.parent.attributes){
            var attr = this.parent.attributes[i];
            result[attr] = this[attr];
        }
        result.id = this.id;
        return result;
    },
    /**
     * 重写JSON.stringfy()默认调用的函数
     * @returns {{}}
     */
    toJSON: function() {
        return (this.attributes());
    },
    createRemote: function(url, callback){
        $.post(url, this.attributes(), callback);
    },
    updateRemote: function(url, callback){
        $.ajax({
            url: url,
            type: 'PUT',
            data: this.attributes(),
            success: callback
        });
    }
});

//添加模型属性
Model.extend({
    /**
     * 返回模型数据集中id为参数的模型实例副本
     * @param id
     * @returns {*}
     */
	find:function(id){
		return this.records[id].dup();
	},
    /**
     * 导入values里所有数据到该模型数据集
     * @param values
     */
	populate: function(values){
        for (var i= 0; i<=values.length; i++) {
            var record = this.init(values[i]);
            record.newRecord = false;
            this.records[record.id] = record;

        }
	}
});

/*********************** Controller and State *************************/

//所有自执行匿名函数使用exports来定义windows属性
var exports = this;

//定义Controller,具有和Class相似的特征
(function($) {
    var mod = {};

    mod.create = function(includes) {
        var result = function() {
            this.init.apply(this, arguments);
        }
        result.fn = result.prototype;
        result.fn.init = function() {};

        result.proxy = function(fn) {return $.proxy(fn, this);};
        result.fn.proxy = result.proxy;

        result.include = function(obj) {$.extend(this.fn, obj);};
        result.extend = function(obj) {$.extend(this, obj)};

        if(includes) {
            result.include(includes);
        }
        return result;
    }

    exports.Controller = mod;

})(jQuery);

(function($, win) {
    var cls = win.Validator = new win.Class();

    cls.include({
        init: function($form, options) {
            this.$form = $form;
            this.options = options;
            this.$btnSubmit = null;
        },
        bindSubmit: function() {
            if (this.options.btnSubmit) {
                this.$btnSubmit = $('#'+this.options.btnSubmit, this.$form);
            } else {
                this.$btnSubmit = $('input[type=submit]', this.$form);
            }
            if (this.$btnSubmit.length !== 1) {
                console.log('Validator cannot position submit button');
                return;
            } else {
                if (this.$btnSubmit.attr('type') === 'submit') {
                    this.$form.bind('submit', this.proxy(function() {
                        this.validate();
                        return false;
                    }));
                } else {
                    this.$btnSubmit.bind('click', this.proxy(this.validate));
                }
                this.bindChange();
            }
        },
        validate: function() {
            var remotes = [];
            var cond = {
                pass: true,
                req: 0,
                fn: this.doSubmit
            };
            var rules = this.options.rules;
            var messages = this.options.messages;
            for (var name in rules) {
                var $elm = $('[name='+name+']', this.$form);
                if ($elm.length > 0) {
                    var rule = rules[name];
                    var message = messages[name];
                    var methods = this.constructor.methods;
                    for (var rn in rule) {
                        var method = methods[rn];
                        var msg = message[rn]
                        if (method) {
                            $elm.next('#tip_' + name).remove();
                            if (rn === 'remote') {
                                var cb = (function($elm, param, msg, cond, method) {
                                    return function() {
                                        method($elm, param, msg, cond);
                                    }
                                })($elm, rule[rn], msg, cond, method);
                                cond.req = cond.req + 1;
                                remotes.push(cb);
                            } else {
                                if (!method($elm, rule[rn])) {
                                    var $tip = $('<label>').attr('id', 'tip_' + name);
                                    $tip.append(msg);
                                    $elm.after($tip);
                                    cond.pass = false;
                                    break;
                                }
                            }
                        } else if(typeof rule[rn] === 'function') {
                            //TODO
                        } else {
                            continue;
                        }
                    }
                }
            }
            if (cond.req > 0) {
                for (var i=-1, len=remotes.length; ++i<len;) {
                    remotes[i]();
                }
            } else {
                if (cond.pass) {
                    this.doSubmit();
                }
            }
        },
        doSubmit: function() {
            alert('ok');
        },
        bindChange: function() {
            var rules = this.options.rules;
            var messages = this.options.messages;
            for (var name in rules) {
                var $elm = $('[name='+name+']', this.$form);
                if ($elm.length > 0) {
                    var rule = rules[name];
                    var message = messages[name];
                    (function($elm, rule, message){
                        $elm.bind('change', this.proxy(function(e) {
                            var methods = this.constructor.methods;
                            for (var rn in rule) {
                                var method = methods[rn];
                                var msg = message[rn]
                                if (method) {
                                    $elm.next('#tip_' + $elm[0].name).remove();
                                    if (rn === 'remote') {
                                        method($elm, rule[rn], msg);
                                    } else {
                                        if (!method($elm, rule[rn])) {
                                            var $tip = $('<label>').attr('id', 'tip_' + $elm[0].name);
                                            $tip.append(msg);
                                            $elm.after($tip);
                                            break;
                                        }
                                    }
                                } else if(typeof rule[rn] === 'function') {
                                    //rule[rn]();
                                } else {
                                    continue;
                                }
                            }
                        }));
                    }).call(this, $elm, rule, message);
                }
            }

        }
    });

    cls.extend({

        init: function($form, options) {
            var o = new this($form, options);
            o.bindSubmit();
            return o;
        },

        methods: {
            required: function($elm, param) {
                var value = $elm.val();
                return value.length > 0;
            },

            remote: function($elm, param, msg, cond) {
                var ajaxParam = null;
                if (param.constructor == String) {
                    ajaxParam = {
                        url: param
                    }
                } else if (param.constructor == Object) {
                    ajaxParam = param;
                } else if (param.constructor == Function) {
                    ajaxParam = param();
                }

                var data = {};
                data[$elm[0].name] = $elm.val();
                if (!ajaxParam.url) {
                    return;
                }
                $.ajax($.extend(true, {
                    type: 'GET',
                    dataType: 'json',
                    data: data,
                    success: function(data) {
                        if (cond != undefined) {
                            if (typeof data === 'boolean') {
                                cond.pass = data && cond.pass;
                            } else if (typeof data === 'string') {
                                cond.pass = ('true' === data) && cond.pass;
                            } else {
                                cond.pass = false;
                            }
                            var req = cond.req = cond.req - 1;
                            if (cond.pass && req === 0) {
                                cond.fn();
                                return;
                            } else if (cond.pass) {
                                return;
                            }
                        } else {
                            if (typeof data === 'boolean') {
                                var pass = data;
                            } else if (typeof data === 'string') {
                                var pass = 'true' === data;
                            } else {
                                var pass =false;
                            }
                            if (pass) {
                                return;
                            }
                        }
                        var $tip = $('<label>').attr('id', 'tip_' + $elm[0].name);
                        $tip.append(msg);
                        $elm.after($tip);
                    },
                    error: function() {
                        if (cond != undefined) {
                            cond.pass = false;
                            //cond.req = cond.req - 1;
                            var $tip = $('<label>').attr('id', 'tip_' + $elm[0].name);
                            $tip.append(msg);
                            $elm.after($tip);
                        }
                    }
                }, ajaxParam))
            }
        }
    });

})(jQuery, window);

