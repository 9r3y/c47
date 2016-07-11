//工具函数库，声明在window.appUtils对象中
(function ($, win) {
    if (win.appUtils) {
        return;
    } else {
        var appUtils = win.appUtils = {};
    }

    // body或#main_section元素
    var _$pageContainer = null;
    /**
     * 返回body或#main_section元素jQuery对象
     * @type {Function}
     * @return {jQuery}
     */
    var getPageContainer = appUtils.getPageContainer = function() {
        if (!_$pageContainer) {
            var $a = $('#main_section');
            if ($a.length === 0) {
                _$pageContainer = $('body');;
            } else {
                _$pageContainer = $a;;
            }
        } else {}
        return _$pageContainer;
    };

    // 保存正在执行的ajax请求
    var _activeXHRs = {};
    appUtils.abortXHRs = function(id) {
        if (!id) {
            for (var id in _activeXHRs) {
                var xhr = _activeXHRs[id]
                delete _activeXHRs[id];
                xhr.abort();
            }
        } else {
            var xhr = _activeXHRs[id];
            if (xhr) {
                delete _activeXHRs[id];
                xhr.abort();
            }
        }
    };

    /**
     * 返回一个对象构造器，使其具有类的特征
     * @param {Function} [Parent] 需继承原型属性的构造器
     * @returns {Function}
     */
    appUtils.createClass = function (Parent) {
        var klass = function () {
            this.init.apply(this, arguments);
        };

        if (Parent) {
            function F() {}
            F.prototype = Parent.prototype;
            var prototype = new F();
            prototype.constructor = klass;
            klass.prototype = prototype;
            klass._super = Parent;
        }

        klass.fn = klass.prototype;
        klass.fn.init = function () {};

        /**
         *
         * 以实例执行环境调用fn函数
         * @param {Function} fn
         */
        klass.fn.proxy = function (fn) {
            var thes = this;
            return function () {
                return fn.apply(thes, arguments);
            };
        };

        /**
         * 扩展类静态方法,obj中的属性将被添加到对象构造器中
         * @param {Object} obj
         */
        klass.extend = function (obj) {
            for (var i in obj) {
                klass[i] = obj[i];
            }
            var extended = obj.extended;
            if (extended)    extended(klass);
        };

        /**
         * 扩展类公共方法,obj中属性将被添加到对象构造器的原型中
         * @param {Object} obj
         */
        klass.include = function (obj) {
            for (var i in obj) {
                klass.fn[i] = obj[i];
            }
            var included = obj.included;
            if (included)    included(klass);
        };

        return klass;
    };

    /**
     * 对话框组件
     * 使用方法：
     * new appUtils.MsgBox({
         title:'aaa',// 对话框标题，可选，默认为"消息"
         msg:'bbb?',// 对话框消息
         icon: appUtils.MsgBox.ICON_WARN,// 感叹号图表，可选
         isModel: false,// 是否遮罩，可选，默认false
         btn:[{// 按钮数组
             type: appUtils.MsgBox.BTN_OK,// 按钮类型
             callback: function() { // 点击按钮后的回调函数, 可选
                 alert('ccc');
             }
         },{
             type: appUtils.MsgBox.BTN_CANCEL
         }]
       }).show();
     * @type {Function}
     */
    appUtils.MsgBox = appUtils.createClass();

    appUtils.MsgBox.extend({
        BTN_OK: 'btnOk',// 确认按钮
        BTN_CANCEL: 'btnCancel',// 取消按钮
        BTN_YES: 'btnYes',// 是按钮
        BTN_NO: 'btnNo',// 否按钮
        ICON_WARN: 'iconWarn'// 警告图标
    });

    appUtils.MsgBox.include({
        init: function (options) {
            this.options = options;
            this.$box = null;
            this.$mask = null;
        },

        //弹出对话框
        show: function () {
            if (!this.options)   return;
            var options = this.options;
            var $box = this.$box = $('<div class="none">');

            var title = this.options.title || '消息';
            var $header = $('<div class="yjq_box_head pr"><h1 class="yjq_box_head_con">' +
                title + '</h1><div class="yth_header_close pa"></div></div>');
            $box.append($header);
            var $btnClose = $header.find('.yth_header_close');

            var msg = this.options.msg || '';
            var $msg = $('<div><p>' + msg + '</p></div>');
            $box.append($msg);

            var $ctrl = $('<div class="tc">');
            $box.append($ctrl);

            //设定样式
            if (this.options.icon && this.options.icon == this.constructor.ICON_WARN) {
                $box.addClass('error_box');
                $msg.addClass('error_box_cont');
                $ctrl.addClass('error_box_conb');
            } else {
                $box.addClass('sure_box');
                $msg.addClass('sure_box_cont');
                $ctrl.addClass('sure_box_conb');
            }

            var thes = this;
            if (this.options.hasOwnProperty('btn')) {
                var ctrl = this.options['btn'];
                $.each(ctrl, function (i, elm) {
                    var type = elm['type'];
                    var callback = elm['callback'];
                    if (type == thes.constructor.BTN_OK) {
                        var $btnOK = $('<input type="button" value="确认" class="ybtna w80 h30 mr20 sure_box_btn">');
                        $btnOK.bind('click', function () {
                            if (callback && typeof(callback) == 'function') {
                                if (callback($box) !== false) {
                                    thes.close();
                                }
                            } else {
                                thes.close();
                            }
                        });
                        $ctrl.append($btnOK);
                    } else if (type == thes.constructor.BTN_CANCEL) {
                        var $btnCancel = $('<input type="button" value="取消" class="ybtn w80 h30 sure_box_btna">');
                        $btnCancel.bind('click', function () {
                            if (callback && typeof(callback) == 'function') {
                                if (callback($box) !== false) {
                                    thes.close();
                                }
                            } else {
                                thes.close();
                            }
                        });
                        $ctrl.append($btnCancel);
                    } else if (type == thes.constructor.BTN_YES) {
                        var $btnYes = $('<input type="button" value="是" class="ybtna w80 h30 mr20 sure_box_btn">');
                        $btnYes.bind('click', function () {
                            if (callback && typeof(callback) == 'function') {
                                if (callback($box) !== false) {
                                    thes.close();
                                }
                            } else {
                                thes.close();
                            }
                        });
                        $ctrl.append($btnYes);
                    } else if (type == thes.constructor.BTN_NO) {
                        var $btnNo = $('<input type="button" value="否" class="ybtn w80 h30 sure_box_btna">');
                        $btnNo.bind('click', function () {
                            if (callback && typeof(callback) == 'function') {
                                if (callback($box) !== false) {
                                    thes.close();
                                }
                            } else {
                                thes.close();
                            }
                        });
                        $ctrl.append($btnNo);
                    }
                });
            }

            if (this.options.isModel) {
                var $mask = this.$mask = $('<div class="error_sure_lay">');
                $mask.appendTo(getPageContainer()).show();
            }

            $btnClose.bind('click', this.proxy(function (e) {
                this.close();
            }));

            $box.appendTo(getPageContainer());
            $box.draggable({containment: "window", scroll: false, handle: $header});
            $box.show();
        },
        close: function() {
            this.$box.remove();
            if (this.$mask != null) {
                this.$mask.remove();
            }
        }
    });

    /**
     * 状态切换器,用户切换页面上的各种状态
     * @type {Function}
     */
    appUtils.StateMachine = appUtils.createClass();

    appUtils.StateMachine.include({
        init: function () {
            this.o = $({});
        },
        bind: function () {
            this.o.bind.apply(this.o, arguments);
        },
        trigger: function () {
            this.o.trigger.apply(this.o, arguments);
        },
        /**
         * 为状态切换器添加控制器，控制器需要实现acitvate和deactive方法，
         * 添加到状态切换器后，该控制器会新增一个active方法，用于激活该控制器
         * @param {Object} controller
         */
        add: function (controller) {
            this.bind('change', function (e, current) {
                if (controller == current) {
                    if (typeof controller.activate === 'function') {
                        controller.activate();
                    }
                } else {
                    if (typeof controller.deactivate === 'function') {
                        controller.deactivate();
                    }
                }
            });

            //为控制器添加active方法
            controller.active = this.proxy(function () {
                this.trigger('change', controller);
            });
        }
    });

    /**
     * 表单校验器
     * @type {Function}
     */
    appUtils.FormValidator = appUtils.createClass();

    appUtils.FormValidator.include({
        /**
         * 校验器构造器
         * @param {jQuery} $form 表单jQuery对象
         * @param {Object} options 校验规则
         */
        init: function ($form, options) {
            this.$form = $form;
            this.options = options;
            this.$btnSubmit = null;
            this._doSubmit = options.submitHandler;
            this.cond = {
                pass: true,
                req: 0,
                fn: this._doSubmit,
                isSubmiting: false
            }
        },
        _bindSubmit: function () {
            if (this.options.btnSubmit) {
                this.$btnSubmit = $('#' + this.options.btnSubmit, this.$form);
            } else {
                this.$btnSubmit = $('input[type=submit]', this.$form);
            }
            if (this.$btnSubmit.length !== 1) {
                console.log('appUtils.FormValidator cannot position submit button');
                return;
            } else {
                if (this.$btnSubmit.attr('type') === 'submit') {
                    var bindEvent = 'submit';
                    var $bindElm = this.$form;
                } else {
                    var bindEvent = 'click';
                    var $bindElm = this.$btnSubmit;
                }
                $bindElm.bind(bindEvent, this.proxy(function () {
                    if (!this.cond.isSubmiting) {
                        this.cond.isSubmiting = true;
                        try {
                            this._validate(this.cond);
                        } catch (e) {
                            console.log('appUtils.FormValidator validate error: ' + e.toString(), e);
                            this.cond.isSubmiting = false;
                        }
                    }
                    if (bindEvent === 'submit') {
                        return false;
                    }
                }));
                this._bindChange();
            }
        },
        _validate: function (cond) {
            cond.req = 0;
            cond.pass = true;
            cond.remotes = [];

            var rules = this.options.rules;
            var messages = this.options.messages;
            for (var name in rules) {
                var $elm = $('[name=' + name + ']', this.$form);
                if ($elm.length > 0) {
                    var rule = rules[name];
                    var message = messages[name];
                    for (var rn in rule) {
                        var msg = message[rn];
                        if (!this._procMethod($elm, rn, rule[rn], msg, cond)) {
                            break;
                        };
                    }
                }
            }
            if (cond.req > 0) {
                for (var i = -1, len = cond.remotes.length; ++i < len;) {
                    cond.remotes[i]();
                }
            } else {
                if (cond.pass) {
                    this._doSubmit();
                }
                cond.isSubmiting = false;
            }
        },

        _doSubmit: $.noop,

        _bindChange: function () {
            var rules = this.options.rules;
            var messages = this.options.messages;
            for (var name in rules) {
                var $elm = $('[name=' + name + ']', this.$form);
                if ($elm.length > 0) {
                    var rule = rules[name];
                    var message = messages[name];
                    (function ($elm, rule, message) {
                        var evtName = $elm.attr('readonly') === 'readonly' ? 'change' : 'focusout';
                        $elm.on(evtName, this.proxy(function (e) {
                            for (var rn in rule) {
                                var msg = message[rn];
                                if (!this._procMethod($elm, rn, rule[rn], msg)) {
                                    break;
                                };
                            }
                        }));
                    }).call(this, $elm, rule, message);
                }
            }
        },

        /**
         * 添加校验规则
         * @param {String} name 输入域的name属性
         * @param {Object} options 规则属性，包含rule和message
         */
        addRule: function (name, options) {
            var rule = this.options.rules[name] = options.rule;
            var message = this.options.messages[name] = options.message;
            var $elm = $('[name=' + name + ']', this.$form);
            var evtName = $elm.attr('readonly') === 'readonly' ? 'change' : 'focusout';
            $elm.on(evtName, this.proxy(function (e) {
                for (var rn in rule) {
                    var msg = message[rn]
                    if (!this._procMethod($elm, rn, rule[rn], msg)) {
                        break;
                    };
                }
            }));
        },

        /**
         * 删除校验规则
         * @param {String} name 输入域的name熟悉
         */
        removeRule: function (name) {
            delete this.options.rules[name];
            delete this.options.messages[name];
            var $elm = $('[name=' + name + ']', this.$form);
            $elm.off('change');
            $elm.next('#tip_' + $elm[0].name).remove();
        },

        /**
         * 处理规则方法
         * @param {jQuery} $elm 输入域元素
         * @param {String} rn 规则名称
         * @param {Object} rule 规则参数
         * @param {String} msg 违反规则提示文字
         * @param {Object} [cond] 校验环境
         * return {Boolean} true则继续校验下一规则，false则停止
         */
        _procMethod: function($elm, rn, rule, msg, cond) {
            var method = this.constructor.methods[rn];
            var msg = msg || '校验不通过';
            if (!method) {
                return true;
            }
            var tipId = 'tip_' + $elm[0].name;
            $elm.next('#' + tipId).remove();
            if (rn === 'remote') {
                if (cond) {
                    var cb = (function ($elm, param, msg, cond, method) {
                        return function () {
                            method($elm, param, msg, cond);
                        }
                    })($elm, rule, msg, cond, method);
                    cond.req = cond.req + 1;
                    cond.remotes.push(cb);
                } else {
                    method($elm, rule, msg);
                }
                return false;
            } else if (rn === 'cutting') {
                if (method($elm, rule)) {
                    return false;
                } else {
                    return true;
                }
            } else {
                if (!method($elm, rule)) {
                    var $tip = $('<label>').attr('id', tipId);
                    $tip.append(appUtils.createInputTip(msg));
                    $elm.after($tip);
                    if (cond) {
                        cond.pass = false;
                    }
                    return false;
                } else {
                    return true;
                }
            }
        }
    });

    appUtils.FormValidator.extend({

        init: function ($form, options) {
            var o = new this($form, options);
            o._bindSubmit();
            return o;
        },

        methods: {
            // 必填域
            required: function ($elm, param) {
                var value = $elm.val();
                return value.length > 0;
            },

            // 主机硬件厂商必填域
            hardwareVendors: function ($elm, param) {
                var value = $elm.val();
                if (value.length > 0) {
                    $("#hardware_vendors_tip").css('display', 'block');
                    return true;
                } else {
                    $("#hardware_vendors_tip").css('display', 'none');
                    return false;
                }
            },

            // 主机负责人必填域
            chargeMan: function ($elm, param) {
                var value = $elm.val();
                if (value.length > 0) {
                    $("#chargeMan_tip").css('display', 'block');
                    return true;
                } else {
                    $("#chargeMan_tip").css('display', 'none');
                    return false;
                }
            },

            // 数据库用户名必填域
            user: function ($elm, param) {
                var value = $elm.val();
                if (value.length > 0) {
                    $("#user_tip").css('display', 'block');
                    return true;
                } else {
                    $("#user_tip").css('display', 'none');
                    return false;
                }
            },

            // 自定义校验条件函数
            judger: function($elm, param) {
                return param();
            },

            // ip域
            ip: function ($elm, param) {
                var value = $elm.val();
                var reg = /^(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/;
                if (reg.exec(value) != null) {
                    if (RegExp.$1 < 0 || RegExp.$1 > 255 || RegExp.$2 < 0 || RegExp.$2 > 255 ||
                        RegExp.$3 < 0 || RegExp.$3 > 255 || RegExp.$4 < 1 || RegExp.$4 > 255) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            },

            // 整数域
            integer: function($elm, param) {
                var value = $elm.val();
                var reg = /^-?\d+$/;
                if (reg.test(value)) {
                    return true;
                } else {
                    return false;
                }
            },

            // 数字域
            number: function ($elm, param) {
                var value = $elm.val();
                return /^-?(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test(value);
            },

            // 短路校验，param是短路条件函数，返回true表示验证通过并不进行后续验证，false表示通过性需要后续验证判断
            cutting: function ($elm, param) {
                return param();
            },

            // 服务端校验，param是返回ajax参数对象的函数
            remote: function ($elm, param, msg, cond) {
                var pm = param();
                var on = pm.on;
                if (on === false) {
                    if (cond != undefined) {
                        cond.req = cond.req - 1;
                        if (cond.req === 0) {
                            cond.isSubmiting = false;
                        }
                    }
                    return;
                }
                var url = pm.url;
                var cb = pm.cb;

                var data = {};
                data[$elm[0].name] = $elm.val();
                if (pm.data) {
                    $.extend(true, data, pm.data);
                }

                var tipId = 'tip_' + $elm[0].name;
                $.ajax(url, {
                    type: 'GET',
                    dataType: 'json',
                    data: data,
                    cache:false,
                    success: function (data) {
                        var cbRet = cb(data);
                        var pass = cbRet === true;
                        if (cond != undefined) {
                            cond.pass = pass && cond.pass;
                            var req = cond.req = cond.req - 1;
                            if (req === 0) {
                                if (cond.pass) {
                                    try {
                                        cond.fn();
                                    } catch (e) {
                                        console.error('appUtils.FormValidator remote validation caught error: '+e.toString(), e);
                                    }
                                    cond.isSubmiting = false;
                                    return;
                                } else {
                                    if (pass) {
                                        cond.isSubmiting = false;
                                        return;
                                    } else {
                                        cond.isSubmiting = false;
                                    }
                                }
                            } else {
                                if (pass) {
                                    return;
                                }
                            }
                        } else {
                            if (pass) {
                                return;
                            }
                        }
                        if ($elm.next('#' + tipId).length === 0) {
                            var $tip = $('<label>').attr('id', tipId);
                            $tip.append(appUtils.createInputTip(cbRet));
                            $elm.after($tip);
                        }
                    },
                    error: function () {
                        if (cond != undefined) {
                            cond.pass = false;
                            var req = cond.req = cond.req - 1;
                            if (req === 0) {
                                cond.isSubmiting = false;
                            }
                        }
                        if ($elm.next('#' + tipId).length === 0) {
                            var $tip = $('<label>').attr('id', tipId);
                            $tip.append(appUtils.createInputTip(msg));
                            $elm.after($tip);
                        }
                    }
                });
            }
        }
    });

    /**
     *
     * 日期格式化器
     * 用法 new appUtils.DateFormat('yyyy-MM-dd').format(new Date());
     * @type {Function}
     */
    appUtils.DateFormat = appUtils.createClass();
    appUtils.DateFormat.include({
        week: {
            "0": "\u65e5",
            "1": "\u4e00",
            "2": "\u4e8c",
            "3": "\u4e09",
            "4": "\u56db",
            "5": "\u4e94",
            "6": "\u516d"
        },
        init: function(pattern) {
            this.pattern = pattern;
        },

        /**
         * 将 Date 转化为指定格式的String
         * 月(M)、日(d)、12小时(h)、24小时(H)、分(m)、秒(s)、周(E)、季度(q) 可以用 1-2 个占位符
         * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
         * eg:
         * "yyyy-MM-dd hh:mm:ss.S" ==> 2006-07-02 08:09:04.423
         * "yyyy-MM-dd E HH:mm:ss" ==> 2009-03-10 二 20:09:04
         * "yyyy-MM-dd EE hh:mm:ss" ==> 2009-03-10 周二 08:09:04
         * "yyyy-MM-dd EEE hh:mm:ss" ==> 2009-03-10 星期二 08:09:04
         * "yyyy-M-d h:m:s.S" ==> 2006-7-2 8:9:4.18
         * @param {Date} date 日期对象
         * @returns {string} 格式化后的字符串
         */
        format: function(date) {
            if (date.constructor !== Date) {
                return '';
            }
            var o = {
                "M+": date.getMonth() + 1, //月份
                "d+": date.getDate(), //日
                "h+": date.getHours() % 12 == 0 ? 12 : date.getHours() % 12, //小时
                "H+": date.getHours(), //小时
                "m+": date.getMinutes(), //分
                "s+": date.getSeconds(), //秒
                "q+": Math.floor((date.getMonth() + 3) / 3), //季度
                "S": date.getMilliseconds() //毫秒
            };
            var fmt = '' + this.pattern;
            if (/(y+)/.test(fmt)) {
                fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
            }
            if (/(E+)/.test(fmt)) {
                fmt = fmt.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "\u661f\u671f" : "\u5468") : "") + this.week[date.getDay() + ""]);
            }
            for (var k in o) {
                if (new RegExp("(" + k + ")").test(fmt)) {
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
                }
            }
            return fmt;
        }
    });

    /**
     * 生成唯一ID
     * @returns {string}
     */
    appUtils.guid = function() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,function (c) {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        }).toUpperCase();
    };

    /**
     * 显示屏蔽层
     * @param {jQuery} [$container] 指定屏蔽的容器 若不指定则屏蔽body元素，若指定则在容器内显示loading动画
     * @param {String} [msg] loading的提示文字
     * @param {String} [closeable] loading是否可被关闭
     */
    appUtils.showMask = function ($container, msg, closeable) {
        if (!$container) {
            var $layerShade = $('<div id="layer_mask" class="layer_shade">');
            $layerShade.appendTo(getPageContainer()).show();
        } else {
            var $layerShade = $container.find('#layer_mask');
            if ($layerShade.length == 0) {
                var width = $container.width();
                var height = $container.height();
                var position = $container.position();
                var left = position.left;
                var top = position.top;
                if (msg == undefined) {
                    var urlGif = App.formatUrl('public/images/loading.gif');
                    $layerShade = $('<div id="layer_mask" style="width:' + width + 'px;height:' + height
                        + 'px;opacity:0.8;background:#fff url(' + urlGif + ') center no-repeat;position:absolute;top:' + top + 'px;left:' + left + 'px;z-index:499;"></div>');
                } else {
                    var urlGif = App.formatUrl('public/images/load_load.gif');
                    $layerShade = $('<div id="layer_mask" style="width:' + width + 'px;height:' + height
                        + 'px;opacity:0.8;background:rgba(0,0,0,.5);position:absolute;top:' + top + 'px;left:' + left + 'px;z-index:499;"></div>');
                    if (closeable !== true) {
                        var msgbox = '<div style="position:relative;top:50%;left:50%;margin:-16px 0 0 -120px; padding: 0 8px; width: 240px; height:32px; border: 1px solid #999999; border-radius: 5px; background: #e9e9e9; line-height: 32px; text-align: center;"><span style="display: inline-block; height: 32px; padding: 0 24px; background: url(' + urlGif + ') no-repeat center left; text-align: center;">' + msg + '</span></div>';
                        $layerShade[0].innerHTML = msgbox;
                    } else {
                        var $msgbox = $('<div class="load_wrap yth_box" id="msgbox_mask">'
                            + '<a href="javascript:void(0)" id="load_close_href" class="yth_header_close snmp_tc_close pa load_close"></a>'
                            + '<div class="load_text clearfix"><img src="' + urlGif + '" alt="" class="load_img"/>'
                            + '<span id="load_con_div" class="load_con">' + msg + '</span></div></div>');
                        $container.append($msgbox);
                        $msgbox.find('#load_close_href').bind('click', function() {
                            $msgbox.remove();
                            $layerShade.remove();
                        });
                    }
                }
                $container.append($layerShade);
            }
        }
    };

    /**
     * 删除屏蔽层
     * @param {jQuery} [$container] 指定删除屏蔽的容器 若不指定则删除屏蔽body元素，若指定则删除在容器内的loading动画
     */
    appUtils.removeMask = function ($container) {
        if (!$container) {
            getPageContainer().children('#layer_mask').remove();
            getPageContainer().children('#msgbox_mask').remove();
        } else {
            $container.children('#layer_mask').remove();
            $container.children('#msgbox_mask').remove();
        }
    };

    // 消息集合
    appUtils.msg = {};
    $.extend(appUtils.msg, {
        MSG_APP_ERR: '程序出现异常',
        MSG_UNKNOWN_ERR: '未知错误',
        MSG_AJAX_CLIENT_ERR: '客户端出现异常',
        MSG_AJAX_SERVER_ERR: '服务端出现异常',
        MSG_AJAX_SEND_FAIL: '请求未能成功发送到服务端'
    });

    /**
     * 处理jquery ajax请求失败
     * @param {Object} xhr
     * @param {String} textStatus
     * @param {String} errorThrown
     */
    appUtils.handleAjaxError = function(xhr, textStatus, errorThrown) {
        var fnself = arguments.callee;
        if (fnself.msgbox === true || !_activeXHRs[xhr.guid]) {
            return;
        }
        fnself.msgbox = true;
        var status = xhr.status;
        var readyState = xhr.readyState;
        var title = appUtils.msg.MSG_APP_ERR;
        var errMsg = appUtils.msg.MSG_UNKNOWN_ERR;
        if (readyState <= 1) {
            title = 'Ready State ' + readyState;
            errMsg = appUtils.msg.MSG_AJAX_SEND_FAIL;
        } else {
            title = status + ' ' + errorThrown ? errorThrown.toString().substr(0, 43) : '';
            if (status >= 400 && status < 500){
                errMsg = appUtils.msg.MSG_AJAX_CLIENT_ERR;
            } else if (status >= 500 && status < 600) {
                errMsg = appUtils.msg.MSG_AJAX_SERVER_ERR;
            } else {
                errMsg = appUtils.msg.MSG_APP_ERR;
            }
        }
        var $errBox = $('<div class="yjq_boxa" id="no_find_box">');
        var str = '<div class="yjq_box_head pr"><h1 class="yjq_box_head_con">' + title +
            '</h1><div class="yth_header_close pa" id="no_find_close"></div></div><div class="yjq_boxa_con">' +
            '<div class="no_find_widget"><div class="no_find_con"><div class="no_find_txt">' + ((!status || ''===status) ? title : status + ' Error') +
            '</div><div class="no_find_tip">' + ((!errorThrown || ''===errorThrown) ? errMsg : title) + '!'
        '</div><div class="no_find_a_widget">' +
        '</div></div></div></div>';
        var $mask = $('<div class="layer_shade"></div>');
        $mask.appendTo(getPageContainer()).show();
        $errBox.appendTo(getPageContainer());
        $errBox[0].innerHTML = str;
        $('#no_find_close', $errBox).bind('click', function() {
            $errBox.remove();
            $mask.remove();
            fnself.msgbox = false;
        });
        $errBox.draggable({containment: "window", scroll: false, handle: $('.yjq_box_head', $errBox)});
        $errBox.show();
        /*new appUtils.MsgBox({
         title: title,
         icon: appUtils.MsgBox.ICON_WARN,
         msg: errMsg,
         btn: [{
         type: appUtils.MsgBox.BTN_OK
         }]
         }).show();*/
    };

    /**
     * 请求加载一个页面
     * @param {String} url 页面的url
     * @param {Object|String} params 请求参数
     * @param {jQuery} $parent 存放页面的父容器
     * @param {jQuery} $masked 加载中需显示loading动画的区域
     * @param {Function} [success] 成功后的回调函数
     * @param {Function} [complete] 完成后回调函数
     */
    appUtils.loadPage = function (url, params, $parent, $masked, success, complete) {
        var thes = this;
        if (typeof params === 'object') {
            var type = 'GET';
            var contentType = 'application/x-www-form-urlencoded';
        } else if (typeof params === 'string') {
            var type = 'POST';
            var contentType = 'application/json';
        }
        return $.ajax(url, {
            type: type,
            data: params,
            contentType: contentType,
            dataType: 'html',
            cache: false,
            beforeSend: function (xhr) {
                xhr.guid = appUtils.guid();
                _activeXHRs[xhr.guid] = xhr;
                if ($masked)   appUtils.showMask($masked);
            },
            success: function (data) {
                $parent.html(data);
                appUtils.initTab($parent);
                if ($masked)   appUtils.removeMask($masked);
                if (success) {
                    success.call(thes, $parent);
                }
            },
            error: function (a, b, c) {
                if ($masked)   appUtils.removeMask($masked);
                appUtils.handleAjaxError(a, b, c);
            },
            complete: function(xhr) {
                delete _activeXHRs[xhr.guid];
                if (complete) {
                    complete();
                }
            }
        });
    };

    /**
     * 请求获取json数据
     * @param {String} url 请求url
     * @param {String} type 请求类型 GET POST PUT DELETE...
     * @param {Object} params 请求参数
     * @param {jQuery} $masked 请求中需显示loading的元素
     * @param {Function} [success] 成功后的回调函数
     * @param {Function} [complete] 完成后回调函数
     */
    appUtils.getJSON = function (url, type, params, $masked, success, complete) {
        if (params) {
            var data = JSON.stringify(params);
        }
        var thes = this;
        return $.ajax(url, {
            url: url,
            type: type,
            contentType: 'application/json',
            data: data,
            dataType: 'json',
            cache: false,
            beforeSend: function (xhr) {
                xhr.guid = appUtils.guid();
                _activeXHRs[xhr.guid] = xhr;
                if ($masked)   appUtils.showMask($masked);
            },
            success: function (data) {
                if ($masked)   appUtils.removeMask($masked);
                success.call(thes, data);
            },
            error: function (a, b, c) {
                if ($masked)   appUtils.removeMask($masked);
                appUtils.handleAjaxError(a, b, c);
            },
            complete: function(xhr) {
                delete _activeXHRs[xhr.guid];
                if (complete) {
                    complete();
                }
            }
        });
    };

    /**
     * 弹出窗口，内容是url返回的视图，回调fn函数
     * @param {String} url
     * @param {String} handle 指定移动窗口的把手区域,传入选择器字符串
     * @param {Function} [fn] 载入弹出窗页面后的回调函数
     */
    appUtils.boxOutUrl = function (url, handle, fn) {
        //var $box=$('<div class="yth_box set_yth_box"></div>');
        var $box = $('<div class="yjq_boxa"></div>');
        var $lb = $('<div class="load_wrap yth_box none">');
        var $lbClose = $('<a href="javascript:void(0)" class="yth_header_close snmp_tc_close pa load_close"></a>');
        $lb.append($lbClose);
        var $lbText = $('<div class="load_text clearfix">');
        $lb.append($lbText);
        var $lbImg = $('<img src="'+win.App.formatUrl('public/images/load_load.gif')+'" alt="" class="load_img"/>');
        $lbText.append($lbImg);
        var $lbSpan = '<span id="load_co_div" class="load_con">正在加载...</span>';
        $lbText.append($lbSpan);

        var stopBeforeShow = false;
        $lbClose.one('click', function() {
            stopBeforeShow = true;
            appUtils.removeMask();
            $lb.remove();
        });

        return $.ajax({
            url: url,
            type: 'GET',
            async: true,
            dataType: 'html',
            cache: false,
            beforeSend: function (xhr) {
                xhr.guid = appUtils.guid();
                _activeXHRs[xhr.guid] = xhr;
                appUtils.showMask();
                $lb.appendTo(getPageContainer()).show();
            },
            success: function (data) {
                if (stopBeforeShow) {
                    return;
                } else {
                    $lb.remove();
                }
                $box.appendTo(getPageContainer());
                try {
                    $box.html(data);
                } catch (e) {
                    $box.remove();
                    appUtils.removeMask();
                    throw e;
                }
                var $btnClose = $('.yth_header_close', $box);
                if ($btnClose.length == 0) {
                    $box.remove();
                    appUtils.removeMask();
                    if (fn) {
                        try {
                            fn();
                        } catch (e) {
                            throw e;
                        }
                    }
                } else {
                    $box.show();
                    $btnClose.bind('click', function () {//隐藏盒子
                        $box.trigger('onboxclose');
                        $box.remove();
                        appUtils.removeMask();
                        return false;
                    });
                    appUtils.initTab($box);
                    $box.draggable({containment: "window", scroll: false, handle: handle});//拖拽盒子
                    if (fn) {
                        try {
                            fn($box, $btnClose);
                        } catch (e) {
                            $box.remove();
                            appUtils.removeMask();
                            throw e;
                        }
                    }
                }
            },
            error: function (a, b, c) {
                appUtils.removeMask();
                $lb.remove();
                appUtils.handleAjaxError(a, b, c);
            },
            complete: function(xhr) {
                delete _activeXHRs[xhr.guid];
            }
        });
    };

    /**
     * 初始化标签页面 要求是ul指定id,如tb1，对应tab content的div指定id为tc_
     * 约定一个ul元素作为标签按钮区，并指定id，内部各li元素作为标签按钮
     * 约定一个div元素标签页面区，并指定id为"tc_"+ul的id，内部div元素作为各标签页面
     * 约定标签按钮的排列顺序和标签页面一致，例如：
     * <ul id="tab">
     *     <li>tabl</li>
     *     <li>tab2</li>
     * </ul>
     * <div id="tc_tab">
     *     <div>tab content 1</div>
     *     <div>tab content 2</div>
     * </div>
     * @param {jQuery} $parent 标签页面父容器
     */
    appUtils.initTab = function ($parent) {
        var $ul = $parent.find('ul');
        $ul.each(function (index, dom) {
            var $this = $(dom);
            var ulId = $this.attr('id');
            var $tc = $parent.find('#tc_' + ulId);
            if ($tc.length == 1) {
                var $lis = $this.children('li');
                $this.delegate('li', 'click', function (e) {
                    var $this = $(this);
                    var i = $lis.index($this);
                    $lis.removeClass('active');
                    $this.addClass('active');

                    $tc.trigger('ontbclicked', i);
                });

                var $tps = $tc.children('div');
                $tc.bind('ontbclicked', function (e, i) {
                    $tps.hide();
                    var $tpActive = $tps.eq(i);
                    $tpActive.show();
                    $tpActive.trigger('onshow');
                    e.stopPropagation();
                });
            }
        });
    };

    /* 初始化toggle按钮
     * @param {jQuery} $btn 按钮
     * @param {String} onMsg 按钮打开状态文字
     * @param {String} offMsg 按钮关闭状态文字
     * @param {Function} onFn 按钮打开回调函数,若返回false则不切换按钮样式
     * @param {Function} offFn 按钮关闭回调函数,若返回false则不切换按钮样式
     */
    appUtils.initToggle = function ($btn, onMsg, offMsg, onFn, offFn) {
        $btn.bind('click', function () {
            var $this = $(this);
            if ($this.hasClass('on')) {
                if (offFn) {
                    if (offFn() !== false) {
                        $this.removeClass("on").text(offMsg);//按钮关闭状态
                    }
                } else {
                    $this.removeClass("on").text(offMsg);//按钮关闭状态
                }
            } else {
                if (onFn) {
                    if (onFn() !== false) {
                        $this.addClass("on").text(onMsg);//按钮打开状态
                    }
                } else {
                    $this.addClass("on").text(onMsg);//按钮打开状态
                }
            }
        });
    };

    /**
     * 为按钮绑定click事件触发弹出确认提示框
     * @param {jQuery} $elm 按钮
     * @param {String} msg 提示信息
     * @param {Function} fn 确认后的回调函数
     */
    appUtils.showConfirmPanel = function ($elm, msg, fn) {
        var $tip = $('<div class="question"></div>').appendTo(getPageContainer());
        var $msg = $('<p class="text"></p>');
        $msg.text(msg);
        $tip.append($msg);
        var $btnConfirm = $('<button class="yes">确定</button>');
        var $btnCancel = $('<button class="cancel">取消</button>');
        $tip.append($btnConfirm);
        $tip.append($btnCancel);
        $tip.css({"top": $elm.offset().top - 64, "left": ($elm.offset().left - (162 - $elm.width()) / 2 )});
        $tip.find(".yes").bind("click", function () {
            $tip.remove();
            if (fn) {
                fn();
            }
        });
        $tip.find(".cancel").bind("click", function () {//取消
            $tip.remove();
        });
        $(document).one("click", function () {
            $tip.remove();
        });
    };

    /**
     * 初始化分页 $parent:tbody   footerId:分页控件容器     fn:回调函
     * @param {Number} count 记录总数
     * @param {Number} pageSize 单页显示数量
     * @param {String} footerId 分页元素容器的id
     * @param {Function} fn 分页状态改变回调函数
     */
    appUtils.initPagination = function (count, pageSize, footerId, fn) {
        var totalNum = count;
        if (!totalNum) {
            totalNum = 0;
            totalPnum = 1;
        } else if (totalNum % pageSize != 0) {
            var totalPnum = parseInt(totalNum / pageSize) + 1;
        } else {
            var totalPnum = parseInt(totalNum / pageSize);
        }
        try {
            $.pagination({
                perPageNum: 9,
                curPnum: 1,
                totalPnum: totalPnum,
                totalNum: totalNum,
                pageDiv: footerId,
                callback: function (pageIndex) {
                    fn(pageIndex);
                }
            });
        } catch (e) {
            console.log('$.pagination caught error: '+ e.toString(), e);
        }
    };

    /**
     * 创建表单输入域错误提示框
     * $param {String} msg 提示消息
     */
    appUtils.createInputTip = function (msg) {
        var $tip = $('<div class="form_tip pr ml10"></div>');
        $tip[0].innerHTML = '<span>' + msg + '</span><em class="form_tip_qua"></em>';
        return $tip;
    };


    /**
     * 使用form/iframe模式上传文件
     * @param opt
     */
    appUtils.uploadFile = function(opt) {
        if (opt.beforeSend) {
            opt.beforeSend();
        }
        opt = $.extend({}, $.ajaxSettings, opt);
        var id = new Date().getTime();
        var frameId = 'jUploadFrame' + id;
        var formId = 'jUploadForm' + id;
        var $form = _createUploadForm(formId, opt.fileElement, opt.data);
        var $frame = _createUploadIframe(frameId, opt.secureuri);
        var requestDone = false;
        var xml = {}
        var uploadCallback = function (e, isTimeout) {
            try {
                var io = $frame[0];
                if (io.contentWindow) {
                    xml.responseText = io.contentWindow.document.body ? io.contentWindow.document.body.innerHTML : null;
                    xml.responseXML = io.contentWindow.document.XMLDocument ? io.contentWindow.document.XMLDocument : io.contentWindow.document;

                } else if (io.contentDocument) {
                    xml.responseText = io.contentDocument.document.body ? io.contentDocument.document.body.innerHTML : null;
                    xml.responseXML = io.contentDocument.document.XMLDocument ? io.contentDocument.document.XMLDocument : io.contentDocument.document;
                }
                if (xml || isTimeout) {
                    requestDone = true;
                    if (!isTimeout) {
                        if (opt.dataType === 'xml') {
                            var data = xml.responseXML;
                        } else {
                            var data = xml.responseText;
                            var start = data.indexOf(">");
                            if(start != -1) {
                                var end = data.indexOf("<", start + 1);
                                if(end != -1) {
                                    data = data.substring(start + 1, end);
                                }
                            }
                            if (opt.dataType === 'json') {
                                eval('data=' + data);
                            }
                        }
                        if (opt.success) {
                            opt.success(data);
                        }
                    }
                } else {
                    if (opt.error) {
                        opt.error();
                    }
                }
            } catch (e) {
                console.log('uploadFile cauth error1 ' + e);
                if (opt.error) {
                    opt.error();
                }
            }
            $frame.remove();
            $form.remove();
            xml = null;
            if (opt.complete) {
                opt.complete();
            }
        }
        if (opt.timeout > 0) {
            setTimeout(function () {
                if (!requestDone) uploadCallback(null, true);
            }, opt.timeout);
        }
        try {
            $form.attr('action', opt.url);
            $form.attr('method', 'POST');
            $form.attr('target', frameId);
            if ($form[0].encoding) {
                $form.attr('encoding', 'multipart/form-data');
            } else {
                $form.attr('enctype', 'multipart/form-data');
            }
            $form.submit();
        } catch (e) {
            console.log('uploadFile cauth error2 ' + e);
            $form.remove();
            $frame.remove();
            return;
        }
        $frame.bind('load', uploadCallback);
    };

    /**
     * 创建接收表单提交返回的Iframe
     * @param id
     * @param uri
     * @returns {*}
     */
    function _createUploadIframe(id, uri) {
        var html = '<iframe id="' + id + '" name="' + id + '" style="position:absolute; top:-9999px; left:-9999px"';
        if (window.ActiveXObject) {
            if (typeof uri == 'boolean') {
                html += ' src="' + 'javascript:false' + '"';
            }
            else if (typeof uri == 'string') {
                html += ' src="' + uri + '"';
            }
        }
        html += ' />';
        var $iframe = $(html);
        $iframe.appendTo(getPageContainer());
        return $iframe;
    }

    /**
     * 创建提交multipart/form-data的表单
     * @param id
     * @param $fileElement
     * @param data
     * @returns {*}
     */
    function _createUploadForm(id, $fileElement, data) {
        var $form = $('<form  action="" method="POST" name="' + id + '" id="' + id + '" enctype="multipart/form-data"></form>');
        if (data) {
            for (var i in data) {
                $('<input type="hidden" name="' + i + '" value="' + data[i] + '" />').appendTo($form);
            }
        }
        var oldElement = $fileElement;
        var newElement = $(oldElement).clone();
        $(oldElement).attr('id', '');
        $(oldElement).before(newElement);
        $(oldElement).appendTo($form);
        $($form).css('position', 'absolute');
        $($form).css('top', '-1200px');
        $($form).css('left', '-1200px');
        $($form).appendTo(getPageContainer());
        return $form;
    }

    /**
     * 按钮点击后出现提示并阻止click事件触发
     * @param {jQuery} $elm
     * @param {String} tip
     */
    appUtils.showClickTip = function($elm, tip) {
        if (!$elm.data("createTooltip")) {
            $elm.data({
                "createTooltip" : true,
                "title" : $elm.attr("title")
            }).tooltip({
                disabled: true,
                show: false,
                content : tip,
                tooltipClass: "unchecked_tooltip",
                position: {
                    my: "center bottom-1",
                    at: "center top"
                },
                close: function (event, ui) {
                    $elm.tooltip("option", "disabled", true).attr("title",$elm.data("title"))
                }
            });
        }
        $elm.tooltip("option",{"disabled" : false, "content" : tip}).tooltip("open").attr("title",$elm.data("title"));
    };

})(jQuery, window);

(function(win) {
    if (win.canvasUtils) {
        return;
    } else {
        var cu = win.canvasUtils = {};
    }

    if (!Array.prototype.indexOf){
        Array.prototype.indexOf = function(elt){
            var len = this.length >>> 0;
            var from = Number(arguments[1]) || 0;
            from = (from < 0) ? Math.ceil(from) : Math.floor(from);
            if (from < 0)from += len;
            for (; from < len; from++){
                if (from in this && this[from] === elt)return from;
            }
            return -1;
        };
    }
    if (!Array.prototype.remove){
        Array.prototype.remove = function(elt){
            for (var i=-1, len=this.length; ++i<len;) {
                if (this[i] === elt) {
                    this.splice(i, 1);
                    break;
                }
            }
        };
    }

    cu.createClass = function (Parent) {
        var klass = function () {
            this.init.apply(this, arguments);
        };
        if (Parent) {
            function F() {}
            F.prototype = Parent.prototype;
            var prototype = new F();
            prototype.constructor = klass;
            klass.prototype = prototype;
            klass._super = Parent;
        }
        klass.fn = klass.prototype;
        klass.fn.init = function () {};
        klass.fn.proxy = function (fn) {
            var thes = this;
            return function () {
                return fn.apply(thes, arguments);
            };
        };
        klass.extend = function (obj) {
            for (var i in obj) {
                klass[i] = obj[i];
            }
            var extended = obj.extended;
            if (extended)    extended(klass);
        };
        klass.include = function (obj) {
            for (var i in obj) {
                klass.fn[i] = obj[i];
            }
            var included = obj.included;
            if (included)    included(klass);
        };
        return klass;
    };

    cu.guid = function() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,function (c) {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        }).toUpperCase();
    };

    win.requestNextAnimationFrame = (function () {
        var originalWebkitMethod,
            wrapper = undefined,
            callback = undefined,
            geckoVersion = 0,
            userAgent = navigator.userAgent,
            index = 0,
            self = this;
        if (window.webkitRequestAnimationFrame) {
            wrapper = function (time) {
                if (time === undefined) {
                    time = +new Date();
                }
                self.callback(time);
            };
            originalWebkitMethod = window.webkitRequestAnimationFrame;
            window.webkitRequestAnimationFrame =
                function (callback, element) {
                    self.callback = callback;
                    originalWebkitMethod(wrapper, element);
                }
        }
        if (window.mozRequestAnimationFrame) {
            index = userAgent.indexOf('rv:');
            if (userAgent.indexOf('Gecko') != -1) {
                geckoVersion = userAgent.substr(index + 3, 3);
                if (geckoVersion === '2.0') {
                    window.mozRequestAnimationFrame = undefined;
                }
            }
        }
        return window.requestAnimationFrame ||
            window.webkitRequestAnimationFrame ||
            window.mozRequestAnimationFrame ||
            window.oRequestAnimationFrame ||
            window.msRequestAnimationFrame ||
            function (callback, element) {
                var start,
                    finish;
                window.setTimeout(function () {
                    start = +new Date();
                    callback(start);
                    finish = +new Date();
                    self.timeout = 1000 / 60 - (finish - start);
                }, self.timeout);
            };
    })();

    var _images = {}
    cu.getImage = function(name) {
        return _images[name];
    };

    var logger = {
        debug: function(msg) {
            if (this.debugLevel >= logger.LEVEL_DEBUG) {
                console.log(msg);
            }
        },
        info: function(msg) {
            if (this.debugLevel >= logger.LEVEL_INFO) {
                console.log(msg);
            }
        },
        warn: function(msg) {
            if (this.debugLevel >= logger.LEVEL_WARN) {
                console.log(msg);
            }
        },
        error: function(msg) {
            if (this.debugLevel >= logger.LEVEL_ERROR) {
                console.log(msg);
            }
        },
        LEVEL_DEBUG: 4,
        LEVEL_INFO: 3,
        LEVEL_WARN: 2,
        LEVEL_ERROR: 1
    };
    logger.debugLevel = logger.LEVEL_INFO;

    cu.loadImages = function(options, fn) {
        var total = options.length;
        var alreadyHave = true;
        for (var i=-1, len=total; ++i<len;) {
            var opt = options[i];
            if (_images[opt.name]) {
                total--;
                continue;
            } else {
                alreadyHave = false;
                var image = new Image();
                image.onload = (function(name, image, opt) {
                    return function() {
                        var a = _images[name] = {};
                        a.image = image;
                        a.width = image.width;
                        a.height = image.height;
                        image.onload = null;
                        total--;
                        if (total === 0 && fn) {
                            logger.info('all asyn image loaded');
                            fn();
                        }
                    }
                })(opt.name, image, opt)
                image.src = opt.url;
            }
        }
        if (alreadyHave && fn) {
            fn();
        }
    };

    cu.g = document.createElement('canvas').getContext('2d');
    //cu.g.font = '12px sans-serif';

    cu.Event = cu.createClass();
    cu.Event.extend({
        CLICK: 'click',
        DBCLICK: 'dblclick',
        MOUSEDOWN: 'mousedown',
        MOUSEUP: 'mouseup',
        MOUSEMOVE: 'mousemove',
        MOUSEOVER: 'mouseover',
        MOUSEOUT: 'mouseout',
        MOUSEWHEEL: 'mousewheel',
        FOCUS: 'focus',
        BLUR: 'blur',
        INVALIDATE: 'invalidate',
        SCROLL: 'scroll',
        addEventListener: function (n, t, f, b) {
            if (b == null)b = false;
            if (n.addEventListener) {// IE9, Chrome
                n.addEventListener(t, f, b);
                if (t === 'mousewheel') {// Firefox
                    n.addEventListener('DOMMouseScroll', f, b);
                }
            } else if (n.attachEvent) {// IE 6/7/8
                n["e" + t + f] = f;
                n[t + f] = function () {
                    n["e" + t + f]();
                };
                n.attachEvent("on" + t, n[t + f]);
            }
        },
        preventDefault: function(e) {
            if (e.preventDefault) {
                e.preventDefault()
            }
            if (e.preventManipulation) {
                e.preventManipulation()
            } else {
                e.returnValue = false
            }
            return false;
        }
    });

    cu.Event.include({
        init: function(type, target, e, p1, p2) {
            this.type = type;
            this.target = target;
            this.e = e ? e : null;// 浏览器事件
            this.p1 = p1 ? p1 : null;// viewRect内坐标
            this.p2 = p2 ? p2 : null;// elmsRect内坐标
        }
    });

    cu.Style = {
        BACKGROUND_COLOR: 'background.color',
        PADDING: 'padding',
        MARGIN: 'margin',
        OUTLINE_WIDTH: 'outline.width',
        OUTLINE_COLOR: 'outline.color',
        INLINE_WIDTH: 'inline.width',
        INLINE_COLOR: 'inline.color',
        SHAPE: 'shape',
        LABEL_DIRECTION: 'label.direction',
        LABEL_GAP: 'label.gap',
        LABEL_PADDING: 'label.padding',
        LABEL_FONT_COLOR: 'label.font.color',
        LABEL_BACKGROUND_COLOR: 'label.background.color',
        LABEL_CORNER_RADIOUS: 'label.corner.radious',
        LABEL_MAXTEXTWIDTH: 'label.maxtextwidth',

        FONT: 'font',
        FONT_COLOR: 'font.color',
        CORNER_RADIOUS: 'corner.radius',

        COLOR: 'color',
        WIDTH: 'scrollbar.width',
        LINE_WIDTH: 'line.width',
        ALPHA: 'alpha'
    };

    cu.EventListener = cu.createClass();
    cu.EventListener.include({
        init: function() {
            this.handlers = {};
        },
        on: function(type, fn) {
            if (!this.handlers[type]) {
                this.handlers[type] = [];
            }
            this.handlers[type].push(fn);
            return this;
        },
        off: function(type, fn) {
            var ls = this.handlers[type];
            if (ls) {
                for (var i=-1, len=ls.length; ++i<len;) {
                    if (ls[i] === fn) {
                        ls.splice(i, 1);
                    }
                }
            }
            return this;
        },
        handleOn: function(type, e, p1, p2) {
            var e = new cu.Event(type, this, e, p1, p2);
            var ls = this.handlers[type];
            if (ls) {
                for (var i=-1, len=ls.length; ++i<len;) {
                    ls[i].call(this, e);
                }
            }
        }
    });

    cu.Rectangle = cu.createClass();
    cu.Rectangle.extend({
        BASE_LEFTUP: 1,
        BASE_CENTER: 2,
        BASE_RIGTHBOTTOM: 3
    });
    cu.Rectangle.include({
        init: function(x, y, w, h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.cx = this.cy = this.r = this.b = null;
            this.update();
        },
        update: function(base) {
            base = base ? base : cu.Rectangle.BASE_LEFTUP;
            switch (base) {
                case cu.Rectangle.BASE_LEFTUP:
                    var hw = this.w / 2;
                    var hh = this.h / 2;
                    this.cx = this.x + hw;
                    this.cy = this.y + hh;
                    this.r = this.x + this.w;
                    this.b = this.y + this.h;
                    break;
                case cu.Rectangle.BASE_CENTER:
                    if (null != this.cx && null != this.cy) {
                        var hw = this.w / 2;
                        var hh = this.h / 2;
                        if (this.cx < hw) {
                            this.x = 0;
                            this.cx = hw;
                        } else {
                            this.x = this.cx - hw;
                        }
                        this.r = this.x + this.w;
                        if (this.cy < hh) {
                            this.y = 0;
                            this.cy = hh;
                        } else {
                            this.y = this.cy - hh;
                        }
                        this.b = this.y + this.h;
                    }
                    break;
                case cu.Rectangle.BASE_RIGTHBOTTOM:
                    if (null != this.r && null != this.b) {
                        if (this.r < this.w) {
                            this.x = 0;
                            this.r = this.w;
                        } else {
                            this.x = this.r - this.w;
                        }
                        this.cx = this.x + this.w / 2;

                        if (this.b < this.h) {
                            this.y = 0;
                            this.b = this.h;
                        } else {
                            this.y = this.b - this.h;
                        }
                        this.cy = this.y + this.h / 2;
                    }
                    break;
            }
        },
        deltaLocation: function(dx, dy) {
            this.x += dx;
            this.y += dy;
            this.update();
        },
        equals: function(that) {
            if (that) {
                return this.x === that.x && this.y === that.y && this.w === that.w && this.h === that.h;
            } else {
                return false;
            }
        },
        isContain: function(rect) {// 是否包含rect
            if (rect) {
                return this.x <= rect.x && this.y <= rect.y && this.r >= rect.r && this.b >= rect.b;
            } else {
                return false;
            }
        },
        isIn: function(x, y) {// 是否包含点x,y
            var l = this.x;
            var r = this.r;
            var t = this.y;
            var b = this.b;
            return x > l && x < r && y > t && y < b;
        },
        intersects: function(rect) {
            var i = this.w;
            var n = this.h;
            var r = rect.w;
            var a = rect.h;
            if (r <= 0 || a <= 0 || i <= 0 || n <= 0) {
                return false
            }
            var o = this.x;
            var s = this.y;
            var A = rect.x;
            var l = rect.y;
            r += A;
            a += l;
            i += o;
            n += s;
            return (r < A || r > o) && (a < l || a > s) && (i < o || i > A) && (n < s || n > l);
        },
        intersection: function(rect) {
            var i = this.x;
            var n = this.y;
            var r = rect.x;
            var a = rect.y;
            var o = i;
            o += this.w;
            var s = n;
            s += this.h;
            var A = r;
            A += rect.w;
            var l = a;
            l += rect.h;
            if (i < r)i = r;
            if (n < a)n = a;
            if (o > A)o = A;
            if (s > l)s = l;
            o -= i;
            s -= n;
            if (o === 0 || s === 0) {
                return null
            }
            return new cu.Rectangle(i, n, o, s);
        },
        copy: function(rect) {
            this.x = rect.x;
            this.y = rect.y;
            this.w = rect.w;
            this.h = rect.h;
            this.update();
        }
    });

    cu.DirtyRect = cu.createClass(cu.Rectangle);
    cu.DirtyRect.include({
        init: function() {
            this.isDirty = null;
            this.reset();
        },
        add: function(rect) {
            if (rect.w !== 0 && rect.h !== 0) {
                this.x = rect.x < this.x ? rect.x : this.x;
                this.r = rect.r > this.r ? rect.r : this.r;
                this.y = rect.y < this.y ? rect.y : this.y;
                this.b = rect.b > this.b ? rect.b : this.b;
                this.w = this.r - this.x;
                this.h = this.b - this.y;
                this.update();
                this.isDirty = true;
            }
        },
        reset: function() {
            var m = Number.MAX_VALUE;
            cu.DirtyRect._super.fn.init.call(this, m, m, -m ,-m);
            this.isDirty = false;
        }
    })

    cu.QuadTree = cu.createClass();
    cu.QuadTree.include({
        init: function(rect) {
            var self = this;
            self.q1 = null;
            self.q2 = null;
            self.q3 = null;
            self.q4 = null;
            self.parent = null;
            self.data = [];
            self.rect = rect;
            self.root = self;

        },
        createChildren:function (deep){
            if (deep == 0)return;
            var self = this;
            var hw = self.rect.width / 2 , hh = self.rect.height / 2;
            self.q1 = new cu.QuadTree(new cu.Rectangle(self.rect.x + hw, self.rect.y, hw, hh));
            self.q2 = new cu.QuadTree(new cu.Rectangle(self.rect.x + hw, self.rect.y + hh, hw, hh));
            self.q3 = new cu.QuadTree(new cu.Rectangle(self.rect.x, self.rect.y + hh, hw, hh));
            self.q4 = new cu.QuadTree(new cu.Rectangle(self.rect.x, self.rect.y, hw, hh));
            self.q1.parent = self.q2.parent = self.q3.parent = self.q4.parent = self;
            self.q1.root = self.q2.root = self.q3.root = self.q4.root = self.root;
            self.q1.createChildren(deep - 1);
            self.q2.createChildren(deep - 1);
            self.q3.createChildren(deep - 1);
            self.q4.createChildren(deep - 1);
        },
        updateChildren: function(rect) {
            this.rect = rect;
            if (this.hasChildren()) {
                var hw = this.rect.w / 2 , hh = this.rect.h / 2;
                this.q1.updateChildren(new cu.Rectangle(this.rect.x + hw, this.rect.y, hw, hh));
                this.q2.updateChildren(new cu.Rectangle(this.rect.x + hw, this.rect.y + hh, hw, hh));
                this.q3.updateChildren(new cu.Rectangle(this.rect.x, this.rect.y + hh, hw, hh));
                this.q4.updateChildren(new cu.Rectangle(this.rect.x, this.rect.y, hw, hh));
            }
        },
        hasChildren:function(){
            return this.q1 || this.q2 || this.q3 || this.q4;
        },
        add: function(v) {
            if (!this.rect.isContain(v.rect)) {
                return null;
            }
            if (!v.rect.isIn(this.rect.cx, this.rect.cy) && this.hasChildren()) {
                var t = this.q1.add(v);
                if (t)  return t;
                t = this.q2.add(v);
                if (t)  return t;
                t = this.q3.add(v);
                if (t)  return t;
                t = this.q4.add(v);
                if (t)  return t;
                //return this.q1.add(v) || this.q2.add(v) || this.q3.add(v) || this.q4.add(v);
            }
            this.data.push(v);
            return this;
        },
        isContain: function(v) {
            return (this.rect.isContain(v.rect) && v.rect.isIn(this.rect.cx, this.rect.cy));
        },
        getDataInRect: function(rect){
            if (!this.rect.intersects(rect)) return [];
            var r = [];
            for (var i=-1, len=this.data.length; ++i<len;) {
                var v = this.data[i];
                if (v.rect.intersects(rect)) {
                    r.push(v);
                }
            }
            if (this.hasChildren()){
                r = r.concat(this.q1.getDataInRect(rect));
                r = r.concat(this.q2.getDataInRect(rect));
                r = r.concat(this.q3.getDataInRect(rect));
                r = r.concat(this.q4.getDataInRect(rect));
            }
            return r;
        },
        getDataOnPoint: function(x, y) {
            if (!this.rect.isIn(x, y)) return [];
            var r = [];
            for (var i=-1, len=this.data.length; ++i<len;) {
                var v = this.data[i];
                if (v.rect.isIn(x, y)) {
                    r.push(v);
                }
            }
            if (this.hasChildren()) {
                r = r.concat(this.q1.getDataOnPoint(x, y));
                r = r.concat(this.q2.getDataOnPoint(x, y));
                r = r.concat(this.q3.getDataOnPoint(x, y));
                r = r.concat(this.q4.getDataOnPoint(x, y));
            }
            return r;
        },
        getData: function() {
            var ret = this.data.concat();
            if (this.hasChildren()) {
                ret = ret.concat(this.q1.getData());
                ret = ret.concat(this.q2.getData());
                ret = ret.concat(this.q3.getData());
                ret = ret.concat(this.q4.getData());
            }
            return ret;
        }
    });

    cu.Layer = cu.createClass();
    cu.Layer.include({
        init: function(id) {
            this.id = id ? id : cu.guid();
            this.rect = new cu.Rectangle(0, 0, 0, 0);
            this.elms= [];
            this.elmId2Tree = {};
            this.changedElm = {};
            this.quadTree = new cu.QuadTree(this.rect);
            this.quadTree.createChildren(3);
            this.isVisible = true;
            this.elmIndex = 0;
        },
        add: function(v) {
            if (this.elms.indexOf(v) === -1) {
                this.increaseElmIndex(v);
                this.elms.push(v);
                this.elmId2Tree[v.id] = null;
                this.changedElm[v.id] = v;
            }
        },
        increaseElmIndex: function(v) {
            v.indexInLayer = ++this.elmIndex;
        },
        batchIncreaseElmIndex: function(arrV) {
            // TODO 框选多个元素时需要批量增加这些元素在同一layer里的索引号
        },
        remove: function(v) {
            this.elms.remove(v);
            if (this.elmId2Tree[v.id]) {
                this.elmId2Tree[v.id].data.remove(v);
                delete this.elmId2Tree[v.id];
            }
        },
        empty: function() {
            var v = null;
            this.elmIndex = 0;
            while ((v = this.elms.pop()) != undefined) {
                if (this.elmId2Tree[v.id]) {
                    this.elmId2Tree[v.id].data.remove(v);
                    delete this.elmId2Tree[v.id];
                }
            }
        },
        update: function(v) {
            if (this.elms.indexOf(v) !== -1) {
                this.changedElm[v.id] = v;
            }
        },
        invalidate: function(context, dirtyRect) {
            var elms = this.quadTree.getDataInRect(dirtyRect);
            elms.sort(function(a, b) {
                return a.indexInLayer - b.indexInLayer;
            });
/*            var tt = [];
            for (var i=-1, len=elms.length; ++i<len;) {
                tt.push(elms[i].indexInLayer);
            }
            console.log(tt.join(','));*/
            for (var i=-1, len=elms.length; ++i<len;) {
                elms[i].draw(context);
            }
        },
        updateQuadTree: function() {
            var r = 0;
            var b = 0;
            var v = null;// 成员
            var t = null;// 树节点
            for (var i=-1, len=this.elms.length; ++i<len;) {
                v = this.elms[i];
                r = v.rect.r > r ? v.rect.r : r;
                b = v.rect.b > b ? v.rect.b : b;
            }
            if (this.rect.r !== r || this.rect.b !== b) {// 重建索引矩形，重新索引所有成员
                this.rect.w = r;
                this.rect.h = b;
                this.rect.update();
                this.quadTree.updateChildren(this.rect);
                for (var i=-1, len=this.elms.length; ++i<len;) {
                    v = this.elms[i];
                    t = this.elmId2Tree[v.id];
                    if (t && !t.isContain(v.rect)) {
                        t.data.remove(v);
                        this.elmId2Tree[v.id] = this.quadTree.add(v);
                    } else if (!t) {
                        this.elmId2Tree[v.id] = this.quadTree.add(v);
                    }
                }
                this.changedElm = {};
            } else {// 重新索引新增或修改的成员
                var v = null;// 成员
                var t = null;// 树节点
                var io = 0;
                for (var h in this.changedElm) {
                    io++;
                }
                for (var id in this.changedElm) {
                    t = this.elmId2Tree[id];
                    v = this.changedElm[id];
                    if (t && !t.isContain(v.rect)) {
                        t.data.remove(v);
                        this.elmId2Tree[v.id] = this.quadTree.add(v);
                    } else if (!t) {
                        this.elmId2Tree[v.id] = this.quadTree.add(v);
                    }
                    delete this.changedElm[id];
                }
            }
        },
        findElementsOnPoint: function(x, y) {
            var elms = this.quadTree.getDataOnPoint(x, y);
/*            var tt = [];
            for (var i = -1, len = elms.length; ++i < len;) {
                tt.push(elms[i].indexInLayer);
            }
            console.log(tt.join(','));*/

            var len = elms.length;
            if (len > 0) {
                elms.sort(function(a, b) {
                    return a.indexInLayer - b.indexInLayer;
                });
            }
            return elms;
        }
    });

    cu.Panel = cu.createClass(cu.EventListener);
    cu.Panel.include({
        init: function(w, h) {
            cu.Panel._super.fn.init.call(this);
            w = w ? w : 330;
            h = h ? h : 150;
            this.scale = 1;// 大于1是放大，小于1是缩小
            this.dirtyRect = new cu.DirtyRect();
            this.isAllDirty = true;
            this.elmsRect = new cu.Rectangle(0, 0, 0, 0);// 包含所有元素的整个区域
            this.viewRect = new cu.Rectangle(0, 0, w, h);// canvas所显示的区域
            this.scrollbar = new cu.Scrollbar(w, h, this.elmsRect, this.viewRect);

            this.canvas = this.createCanvas(w, h);
            this.div = this.createPanelDiv(this.canvas, w, h);
            this.context = this.canvas.getContext('2d');
            this.layers = [];// 先放入的位于底层
            this.layers.push(new cu.Layer());

            this.addEventListeners();

            this.elmHover = null;
            this.hoverPointP1 = null;
            this.hoverPointP2 = null;
            this.elmSelected = null;

            this.isElmInvalidateLocked = false;
            this.isHandToolControlled = false;
            this.isPressed = false;
        },
        createCanvas: function(w, h) {
            var cvs = document.createElement('canvas');
            var st = cvs.style;
            st.position = 'absolute';
            //st.background = "#CCE8CF";
            cvs.width = w;
            cvs.height = h;
            return cvs;
        },
        createPanelDiv: function(canvas, w, h) {
            var div = document.createElement('div');
            var st = div.style;
            st.width = w + 'px';
            st.height = h + 'px';
            st.position = 'relative';
            st.overflow = 'hidden';
            st.fontSize = "12px";
            st.fontFamily = "arial, tahoma, sans-serif, helvetica";
            st.cursor = "default";
            div.onmousedown = cu.Event.preventDefault;
            div.appendChild(canvas);
            return div;
        },
        getWidth: function() {
            return this.canvas.width;
        },
        getHeight: function() {
            return this.canvas.height;
        },
        setWidth: function(w) {
            if (this.canvas.width === w)    return;
            this.div.style.width = w + 'px';
            this.canvas.width = w;
            this.scrollbar.width = w;
            var vr = this.viewRect;
            var er = this.elmsRect;
            vr.w = w / this.scale;
            if (vr.w >= er.w) {
                vr.x = 0;
                vr.update();
                this.isAllDirty = true;
            } else {
                var r = vr.x + vr.w;
                if (r > er.r) {
                    vr.r = er.r + this.scrollbar.barWidth;
                    vr.update(cu.Rectangle.BASE_RIGTHBOTTOM);
                    this.isAllDirty = true;
                }
            }
            this.scrollbar.update();
        },
        setHeight: function(h) {
            if (this.canvas.height === h)   return;
            this.div.style.height = h + 'px';
            this.canvas.height = h;
            this.scrollbar.height = h;
            var vr = this.viewRect;
            var er = this.elmsRect;
            vr.h = h / this.scale;
            if (vr.h >= er.h) {
                vr.y = 0;
                vr.update();
                this.isAllDirty = true;
            } else {
                var b = vr.y + vr.h;
                if (b > er.b) {
                    vr.b =er.b + this.scrollbar.barWidth;
                    vr.update(cu.Rectangle.BASE_RIGTHBOTTOM);
                    this.isAllDirty = true;
                }
            }
            this.scrollbar.update();
        },
        setScale: function(scale) {
            if (scale <= 0 || scale > 2)    throw 'scale must be in (0,2)';
            this.scale = scale;
            var vr = this.viewRect;
            var er = this.elmsRect;
            vr.w = this.canvas.width / scale;
            vr.h = this.canvas.height / scale;
            vr.update(vr.r === er.r || vr.b === er.b ? cu.Rectangle.BASE_RIGTHBOTTOM : null);
            if (vr.r > er.r || vr.b > er.b) {
                vr.r = vr.r > er.r ? er.r : vr.r;
                vr.b = vr.b > er.b ? er.b : vr.b;
                vr.update(cu.Rectangle.BASE_RIGTHBOTTOM);
            }
            this.scrollbar.update();
            this.isAllDirty = true;
        },
        getFitPanelScale: function() {
            var a = this.canvas.width / this.elmsRect.w;
            var b = this.canvas.height / this.elmsRect.h;
            a = a < b ? a : b;
            return a;
        },
        setHandToolEnabled: function(bool) {
            this.isHandToolControlled = bool;
            this.canvas.style.cursor = bool ? 'hand' : 'default';
        },
        export2PngBase64: function() {
            var er = this.elmsRect;
            var cvs = this.createCanvas(er.w, er.h);
            var c = cvs.getContext('2d');
            for (var i=-1, len=this.layers.length; ++i<len;) {
                this.layers[i].invalidate(c, this.elmsRect);
            }
            var data = cvs.toDataURL('image/png');
            var i = data.indexOf(',');
            return data.slice(i+1);
        },
        setStyle: function(k, v) {
            var st = this.div.style;
            st[k] = v;
        },
        addElement: function(v) {
            v.panel = this;
            if (!v.layer) {
                v.layer = this.layers[0];
                this.layers[0].add(v);
            } else {
                v.layer.add(v);
            }
            v.calcRect();
            this.dirtyRect.add(v.rect);
            v.dirtyRect.reset();
            v.on(cu.Event.INVALIDATE, this.proxy(this.handleElementInvalidate));
        },
        empty: function() {
            for (var i=-1, len=this.layers.length; ++i<len;) {
                this.layers[i].empty();
            }
            this.elmHover = null;
            this.hoverPointP1 = null;
            this.hoverPointP2 = null;
            this.elmSelected = null;
            this.isScrollbarHovered = false;
            var er = this.elmsRect;
            var vr = this.viewRect;
            er.w = er.h = 0;
            er.update();
            vr.w = this.canvas.width;
            vr.h = this.canvas.height;
            vr.update();
            this.isElmInvalidateLocked = false;
            this.setHandToolEnabled(false);
            this.isPressed = false;
            this.scale = 1;
            this.scrollbar.update();
            this.isAllDirty = true;
            this.invalidate();
        },
        handleElementInvalidate: function(e) {
            this.dirtyRect.add(e.target.dirtyRect);
            e.target.layer.update(e.target);
            if (!this.isElmInvalidateLocked) {
                this.invalidate();
            }
        },
        addLayer: function(layer) {
            this.layers.push(layer);
        },
        lockElementInvalidate: function() {
            this.isElmInvalidateLocked = true;
        },
        invalidate: function() {
            this.isElmInvalidateLocked = false;
            if (this.calcElmsRect()) {
                this.scrollbar.update();
                this.isAllDirty = true;
            }
            this.draw();
        },
        calcElmsRect: function() {
            var r = 0;
            var b = 0;
            var er = this.elmsRect;
            var vr = this.viewRect;
            for (var i=-1, len=this.layers.length; ++i<len;) {
                var v = this.layers[i];
                v.updateQuadTree();
                r = v.rect.r > r ? v.rect.r : r;
                b = v.rect.b > b ? v.rect.b : b;
            }
            var vrChanged = false;
            if (er.w !== r) {
                er.w = r;
                er.update();
                if (vr.w >= er.w && vr.x !== 0) {
                    vr.x = 0;
                    vr.update();
                    vrChanged = true;
                } else {
                    var r = vr.x + vr.w;
                    if (r > er.r) {
                        vr.r = er.r + this.scrollbar.barWidth;
                        vr.update(cu.Rectangle.BASE_RIGTHBOTTOM);
                        vrChanged = true;
                    }
                }
            }
            if (er.h !== b) {
                er.h = b;
                er.update();
                if (vr.h >= er.h && vr.y !== 0) {
                    vr.y = 0;
                    vr.update();
                    vrChanged = true;
                } else {
                    var b = vr.y + vr.h;
                    if (b > er.b) {
                        vr.b = er.b + this.scrollbar.barWidth;
                        vr.update(cu.Rectangle.BASE_RIGTHBOTTOM);
                        vrChanged = true;
                    }
                }
            }
            return vrChanged;
        },
        draw: function() {
            var c = this.context;
            var vr = this.viewRect;
            var scale = this.scale;
//            var r = vr;
            if (this.isAllDirty) {
                var r = vr;
                this.isAllDirty = false;
            } else {
                var r = this.dirtyRect.intersection(this.viewRect);
            }

            c.save();
            c.scale(scale, scale);
            c.translate(-vr.x, -vr.y);
            c.clearRect(r.x-1, r.y-1, r.w+2, r.h+2);
            c.beginPath();
            c.rect(r.x-2, r.y-2, r.w+4, r.h+4);
            c.clip();
            for (var i=-1, len=this.layers.length; ++i<len;) {
                this.layers[i].invalidate(c, r);
            }
            c.restore();
            this.dirtyRect.reset();

            this.scrollbar.draw(c);
        },
        findElement: function(id) {
            var layer = null;
            var elm = null;
            for (var i=-1, lenI=this.layers.length; ++i<lenI;) {
                layer = this.layers[i];
                for (var j=-1, lenJ=layer.elms.length; ++j<lenJ;) {
                    elm = layer.elms[j];
                    if (id === elm.id) {
                        return elm;
                    }
                }
            }
        },
        windowPoint2CanvasPoint: function(x, y) {
            var bbox = this.canvas.getBoundingClientRect();
            return { x: x - bbox.left * (this.canvas.width / bbox.width),
                y: y - bbox.top * (this.canvas.height / bbox.height)};
        },
        canvasPoint2windowPoint: function(x, y) {
            var bbox = this.canvas.getBoundingClientRect();
            return { x: x + bbox.left * (this.canvas.width / bbox.width),
                y: y + bbox.top * (this.canvas.height / bbox.height)};
        },
        viewRectPoint2ElmsPoint: function(x, y) {
            return {x: x / this.scale + this.viewRect.x, y: y / this.scale + this.viewRect.y};
        },
        elmsPoint2ViewRectPoint: function(x, y) {
            return {x: (x - this.viewRect.x) * this.scale, y: (y - this.viewRect.y) * this.scale};
        },
        addEventListeners: function() {
            var f = cu.Event.addEventListener;
            f(this.div, cu.Event.CLICK, this.proxy(this.onClick));
            f(this.div, cu.Event.DBCLICK, this.proxy(this.onDblClick));
            f(this.div, cu.Event.MOUSEDOWN, this.proxy(this.onMousedown));
            f(this.div, cu.Event.MOUSEUP, this.proxy(this.onMouseup));
            f(this.div, cu.Event.MOUSEMOVE, this.proxy(this.onMousemove));
            f(this.div, cu.Event.MOUSEOVER, this.proxy(this.onMouseover));
            f(this.div, cu.Event.MOUSEOUT, this.proxy(this.onMouseout));
            f(this.div, cu.Event.MOUSEWHEEL, this.proxy(this.onMouseWheel));
            f(document, cu.Event.MOUSEMOVE, this.proxy(this.onMousemoveOutside));
            f(document, cu.Event.MOUSEUP, this.proxy(this.onMouseupOutside));

            this.scrollbar.on(cu.Event.SCROLL, this.proxy(this.onScroll));
        },
        onMouseWheel: function(e) {
            var e = window.event || e; // old IE support
            var delta = Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail)));
            this.scrollbar.scroll(null, -delta * 10);
            return false;
        },
        onClick: function(e) {
            var p1 = this.windowPoint2CanvasPoint(e.clientX, e.clientY);
            var p2 = this.viewRectPoint2ElmsPoint(p1.x, p1.y);
            if (this.elmHover && this.elmHover.isSelectable) {
                this.elmHover.handleOn(cu.Event.CLICK, e, p1, p2);
            } else {

            }
            this.handleOn(cu.Event.CLICK, e, p1, p2);
            return false;
        },
        onDblClick: function(e) {
            var p1 = this.windowPoint2CanvasPoint(e.clientX, e.clientY);
            var p2 = this.viewRectPoint2ElmsPoint(p1.x, p1.y);
            if (this.elmHover && this.elmHover.isSelectable) {
                this.elmHover.handleOn(cu.Event.DBCLICK, e, p1, p2);
            } else {
            }
            this.handleOn(cu.Event.DBCLICK, e, p1, p2);
            return false;
        },
        onMousedown: function(e) {
            var p1 = this.hoverPointP1 = this.windowPoint2CanvasPoint(e.clientX, e.clientY);
            var p2 = this.hoverPointP2 = this.viewRectPoint2ElmsPoint(p1.x, p1.y);
            if (this.isScrollbarHovered) {
                this.scrollbar.handleOn(cu.Event.MOUSEDOWN, e, p1, p2);
                return;
            }

            if (this.isHandToolControlled) {
                this.isPressed = true;
                return;
            }
            if (this.elmHover) {
                if (this.elmHover.isSelectable) {
                    this.elmHover.handleOn(cu.Event.MOUSEDOWN, e, p1, p2);
                    if (!this.elmSelected) {
                        this.elmSelected = this.elmHover;
                        this.elmSelected.layer.increaseElmIndex(this.elmSelected);
                        this.elmSelected.handleOn(cu.Event.FOCUS, e, p1, p2);
                    } else if (this.elmSelected !== this.elmHover) {
                        this.elmSelected.handleOn(cu.Event.BLUR, e, p1, p2);
                        this.elmSelected = this.elmHover;
                        this.elmSelected.layer.increaseElmIndex(this.elmSelected);
                        this.elmSelected.handleOn(cu.Event.FOCUS, e, p1, p2);
                    }
                } else {
/*                    if (this.elmSelected) {
                        this.elmSelected.handleOn(cu.Event.BLUR, e, p1, p2);
                        this.elmSelected = null;
                    }*/
                }
            } else {
/*                if (this.elmSelected) {
                    this.elmSelected.handleOn(cu.Event.BLUR, e, p1, p2);
                    this.elmSelected = null;
                }*/
            }
            this.handleOn(cu.Event.MOUSEDOWN, e, p1, p2);
            return false;
        },
        onMouseup: function(e) {
            if (this.isScrollbarHovered) {
                this.scrollbar.handleOn(cu.Event.MOUSEUP, e);
            } else if (this.isHandToolControlled) {
                this.isPressed = false;
            } else if (this.elmHover) {
                this.elmHover.handleOn(cu.Event.MOUSEUP, e);
            }
            this.handleOn(cu.Event.MOUSEUP, e, null, null);
            return false;
        },
        onMousemove: function(e) {
            var p1 = this.windowPoint2CanvasPoint(e.clientX, e.clientY);
            var p2 = this.viewRectPoint2ElmsPoint(p1.x, p1.y);
            var oldHoverPointP1 = this.hoverPointP1;
            var oldHoverPointP2 = this.hoverPointP2;
            this.hoverPointP1 = p1;
            this.hoverPointP2 = p2;

            if (this.isScrollbarHovered && this.scrollbar.isPressed) {
                this.scrollbar.handleOn(cu.Event.MOUSEMOVE, e, p1, p2);
                return;
            }

            if (this.scrollbar.isOnHandle(p1.x, p1.y)) {
                if (!this.isScrollbarHovered) {
                    this.scrollbar.handleOn(cu.Event.MOUSEOVER, e, p1, p2);
                    this.isScrollbarHovered = true;
                }
                this.scrollbar.handleOn(cu.Event.MOUSEMOVE, e, p1, p2);
                return;
            } else {
                if (this.isScrollbarHovered) {
                    this.isScrollbarHovered = false;
                    this.scrollbar.handleOn(cu.Event.MOUSEOUT, e, p1, p2);
                }
            }

            if (this.isHandToolControlled) {
                if (this.isPressed) {
                    var deltaX = (p1.x - oldHoverPointP1.x) / this.scale;
                    var deltaY = (p1.y - oldHoverPointP1.y) / this.scale;
                    var vr = this.viewRect;
                    var er = this.elmsRect;
                    var x = vr.x - deltaX;
                    var y = vr.y - deltaY;
                    x = x < 0 ? 0 : x;
                    y = y < 0 ? 0 : y;
                    var a = er.r - vr.w + this.scrollbar.barWidth;
                    a = a < 0 ? 0 : a;
                    x = x > a ? a : x;
                    a = er.b - vr.h + this.scrollbar.barWidth;
                    a = a < 0 ? 0 : a;
                    y = y > a ? a : y;
                    vr.x = x;
                    vr.y = y;
                    vr.update();
                    this.scrollbar.update();
                    this.isAllDirty = true;
                    this.invalidate();
                }
                return;
            }

            if (this.elmHover && this.elmHover.isPressed) {
                this.elmHover.handleOn(cu.Event.MOUSEMOVE, e, p1, p2);
            } else {
                var elmHover = null;
                for (var i=this.layers.length, lenI=-1; --i>lenI;) {
                    var layer = this.layers[i];
                    var elms = layer.findElementsOnPoint(p2.x, p2.y);
                    for (var j=elms.length, lenJ=-1; --j>lenJ;) {
                        var elm = elms[j];
                        if (elm.isHoverable && elm.canHover()) {
                            elmHover = elm;
                            if (!this.elmHover) {
                                this.elmHover = elmHover;
                                elmHover.handleOn(cu.Event.MOUSEOVER, e, p1, p2);
                            } else if (this.elmHover !== elmHover) {
                                this.elmHover.handleOn(cu.Event.MOUSEOUT, e, p1, p2);
                                this.elmHover = elmHover;
                                elmHover.handleOn(cu.Event.MOUSEOVER, e, p1, p2);
                            }
                            elmHover.handleOn(cu.Event.MOUSEMOVE, e, p1, p2);
                            break;
                        }
                    }
                    if (elmHover) {
                        break;
                    }
                }
                if (!elmHover) {
                    // 移动到空白
                    if (this.elmHover) {
                        this.elmHover.handleOn(cu.Event.MOUSEOUT, e, p1, p2);
                        this.elmHover = null;
                    }
                }
            }
            return false;
        },
        onMouseover: function(e) {
            this.handleOn(cu.Event.MOUSEOVER, e, null, null);
            return false;
        },
        onMouseout: function(e) {
/*            if (this.elmHover) {
                if (this.elmHover.isPressed) {
                    return;
                }
                this.elmHover.handleOn(cu.Event.MOUSEOUT, e);
                this.hoverPointP1 = null;
                this.hoverPointP2 = null;
                this.elmHover = null;
            }*/
            cu.tip.hide();
            this.handleOn(cu.Event.MOUSEOUT, e, null, null);
            return false;
        },
        onScroll: function(e) {
            this.isAllDirty = true;
            this.invalidate();
        },
        onMousemoveOutside: function(e) {
            if (this.isScrollbarHovered && this.scrollbar.isPressed) {
                var p1 = this.windowPoint2CanvasPoint(e.clientX, e.clientY);
                this.scrollbar.handleOn(cu.Event.MOUSEMOVE, e, p1, null);
            }
        },
        onMouseupOutside: function(e) {
            if (this.isScrollbarHovered && this.scrollbar.isPressed) {
                this.scrollbar.handleOn(cu.Event.MOUSEUP, e);
            }
        }
    });

    cu.Tip = cu.createClass();
    cu.Tip.include({
        init: function() {
            this.divTip = this.getTipDiv();
            this.appended = false;
            this.timerId = null;
        },
        draw: function(context, x, y, mw, mh, text) {//mw mh是canvas元素当前宽长
            y+=18;
            var metric = cu.g.measureText(text);
            var textWidth = metric.width;
            if (textWidth > cu.Tip.MAX_TEXT_WIDTH) {
                textWidth = cu.Tip.MAX_TEXT_WIDTH;
            }
            var lineCount = Math.ceil(metric.width / cu.Tip.MAX_TEXT_WIDTH);
            var lineCharCount = Math.round(text.length * cu.Tip.MAX_TEXT_WIDTH / metric.width);
            var width = Math.round(textWidth + 2*(this.padding + this.outlineWidth));
            var height = Math.round(this.fontSize*(lineCount+1) + this.padding*lineCount + 2*(this.padding + this.outlineWidth));

            x = Math.round(x+width > mw ? mw-width : x) - 0.5;
            y = Math.round(y+height > mh ? mh-height : y) - 0.5;

            this.imgData = context.getImageData(x, y, width, height);
            this.dx = x;
            this.dy = y;

            cu.drawRoundRect(context, x+this.outlineWidth/2, y+this.outlineWidth/2,
                width-this.outlineWidth, height-this.outlineWidth, 5);
            context.save();
            context.strokeStyle = '#000000';
            context.fillStyle = '#FFFFE0';
            context.stroke();
            context.fill();
            context.fillStyle = '#000000';
            context.textAlign = "left";
            context.textBaseline = "top";

            var offSetX = x + this.padding;
            var offSetY = y + this.padding;
            for (var i=0, j=0, len=text.length; i<=lineCount; i++) {
                var k = j + lineCharCount;
                if (k > len) {
                    k = len;
                }
                context.fillText(text.substring(j, k), offSetX, offSetY, cu.Tip.MAX_TEXT_WIDTH);
                offSetY += (this.fontSize + this.padding);
                j=k;
            }

            context.restore();
        },
        getTipDiv: function() {
            var div = document.createElement("div");
            div.className = "tp-tooltip";
            return div;
        },
        show: function(text, panel, p1) {
            if (this.appended) {
                win.appUtils.getPageContainer()[0].removeChild(this.divTip);
                this.appended = false;
            }
            this.timerId = win.setTimeout(this.proxy(function() {
                var p = panel.canvasPoint2windowPoint(p1.x, p1.y);
                var div = this.divTip;
                div.innerHTML = text;
                div.style.left = p.x + 'px';
                div.style.top = p.y + 18 + 'px';
                win.appUtils.getPageContainer()[0].appendChild(div);
                this.appended = true;
            }), cu.Tip.DELAY);
        },
        hide: function() {
            if (this.timerId !== -1) {
                win.clearTimeout(this.timerId);
                this.timerId = -1;
            }
            if (this.appended) {
                win.appUtils.getPageContainer()[0].removeChild(this.divTip);
                this.appended = false;
            }
        }
    });
    cu.Tip.extend({
        DELAY: 500,
        MAX_TEXT_WIDTH: 160
    });
    cu.tip = new cu.Tip();

    cu.Element = cu.createClass(cu.EventListener);
    cu.Element.include({
        init: function(id) {
            cu.Element._super.fn.init.call(this);
            this.id = id ? id : cu.guid();
            this.panel = null;
            this.layer = null;
            this.indexInLayer = null;
            this.rect = new cu.Rectangle(0, 0, 0, 0);
            this.dirtyRect = new cu.DirtyRect();
            this.style = {};
            this.customData = {};
            this.isHoverable = true;
            this.isSelectable = true;
            this.isDraggable = true;
            this.hoverPointP1 = null;
            this.hoverPointP2 = null;
            this.isPressed = false;
            this.isFoucus = false;

            this.isDisplayChanged = true;
            this.on(cu.Event.CLICK, this.proxy(this.onClick));
            //this.on(cu.Event.DBCLICK, this.proxy(this.onDblClick));
            this.on(cu.Event.MOUSEDOWN, this.proxy(this.onMousedown));
            this.on(cu.Event.MOUSEUP, this.proxy(this.onMouseup));
            this.on(cu.Event.MOUSEMOVE, this.proxy(this.onMousemove));
            this.on(cu.Event.MOUSEOVER, this.proxy(this.onMouseover));
            this.on(cu.Event.MOUSEOUT, this.proxy(this.onMouseout));
            this.on(cu.Event.FOCUS, this.proxy(this.onFocus));
            this.on(cu.Event.BLUR, this.proxy(this.onBlur));
        },
        setLocation: function(x, y) {
            var r = this.rect;
            this.dirtyRect.add(r);
            this.doCalcRect();
            var oldX = r.x;
            var oldY = r.y;
            r.x = x;
            r.y = y;
            this.rect.update(cu.Rectangle.BASE_LEFTUP);
            if (r.x !== oldX || r.y !== oldY) {
                this.onLocationChanged(r.x-oldX, r.y-oldY);
            }
            this.dirtyRect.add(r);
        },
        setCenterLocation: function(x, y) {
            var r = this.rect;
            this.dirtyRect.add(r);
            this.doCalcRect();
            var oldX = r.x;
            var oldY = r.y;
            r.cx = x;
            r.cy = y;
            r.update(cu.Rectangle.BASE_CENTER);
            if (r.x !== oldX || r.y !== oldY) {
                this.onLocationChanged(r.x-oldX, r.y-oldY);
            }
            this.dirtyRect.add(r);
        },
        setLayer: function(layer) {
            if (this.layer) {
                this.layer.remove(this);
                this.layer = layer;
                layer.add(this);
            } else {
                this.layer = layer;
            }
        },
        setStyle: function(k, v) {
            var st = this.style;
            var o = st[k];
            if (o !== v) {
                st[k] = v;
                this.isDisplayChanged = true;
            }
        },
        calcRect: function() {
            if (this.isDisplayChanged) {
                this.doCalcRect();
                this.isDisplayChanged = false;
            }
        },
        doCalcRect: function() {
        },
        invalidate: function() {
            this.calcRect();
            this.dirtyRect.add(this.rect);
            this.handleOn('invalidate');
            this.dirtyRect.reset();
        },
        draw: function(context) {
        },
        canHover: function() {
            return true;
        },
        onClick: function(e) {
            logger.debug('elm click');
        },
        onDblClick: function(e) {
            logger.debug('elm dbclick');
        },
        onMousedown: function(e) {
            logger.debug('elm mousedown');
            this.isPressed = true;
        },
        onMouseup: function(e) {
            logger.debug('elm mouseup');
            this.isPressed = false;
        },
        onMousemove: function(e) {
            if (this.isPressed && this.isDraggable) {
                var dx = e.p2.x - this.hoverPointP2.x;
                var dy = e.p2.y - this.hoverPointP2.y;
                this.hoverPointP1.x = e.p1.x;
                this.hoverPointP1.y = e.p1.y;
                this.hoverPointP2.x = e.p2.x;
                this.hoverPointP2.y = e.p2.y;
                var newX = this.rect.x + dx;
                var newY = this.rect.y + dy;
                newX = newX < 0 ? 0 : newX;
                newY = newY < 0 ? 0 : newY;
                this.setLocation(newX, newY);
                this.invalidate();
            } else {
                this.hoverPointP1.x = e.p1.x;
                this.hoverPointP1.y = e.p1.y;
                this.hoverPointP2.x = e.p2.x;
                this.hoverPointP2.y = e.p2.y;
            }
        },
        onMouseover: function(e) {
            this.hoverPointP1 = e.p1;
            this.hoverPointP2 = e.p2;
            logger.debug('mouseover');
        },
        onMouseout: function(e) {
            this.hoverPointP1 = null;
            this.hoverPointP2 = null;
            logger.debug('mouseout');
        },
        onFocus: function(e) {
            logger.debug('focus');
            this.isFoucus = true;

        },
        onBlur: function(e) {
            logger.debug('blur');
            this.isFoucus = false;

        },
        onLocationChanged: function(dx, dy) {
        },
        data: function(k, v) {
            if (v) {
                this.customData[k] = v;
                return v;
            } else if (k) {
                return this.customData[k];
            } else {
                return this.customData;
            }
        }
    });

    cu.Scrollbar = cu.createClass(cu.EventListener);
    cu.Scrollbar.include({
        init: function(w, h, elmsRect, viewRect) {
            cu.Scrollbar._super.fn.init.call(this);
            this.barWidth = 8;
            this.cornerRadious = 4;
            this.width = w;
            this.height = h;

            this.elmsRect = elmsRect;
            this.viewRect = viewRect;
            this.rectX = null;
            this.rectY = null;

            this.offsetX = 0;// x轴偏移量
            this.offsetY = 0;
            this.OFFSET_MAX_X = null;
            this.OFFSET_MAX_Y = null;

            this.handleSizeX = null;
            this.handleSizeY = null;

            this.isSelectable = false;
            this.isPressed = false;
            this.hoverType = 0;// 1表示x轴，2表示y轴
            this.hoverPointP1 = null;

            this.isVisible = true;
            this.isXScrollable = false;
            this.isYScrollable = false;
            if (elmsRect.w > 0 && elmsRect.h > 0) {
                this.update();
            } else {
                this.isXScrollable = false;
                this.isYScrollable = false;
            }

            this.on(cu.Event.MOUSEDOWN, this.proxy(this.onMousedown));
            this.on(cu.Event.MOUSEUP, this.proxy(this.onMouseup));
            this.on(cu.Event.MOUSEMOVE, this.proxy(this.onMousemove));
            this.on(cu.Event.MOUSEOVER, this.proxy(this.onMouseover));
            this.on(cu.Event.MOUSEOUT, this.proxy(this.onMouseout));

        },
        update: function() {
            var elmsRect = this.elmsRect;
            var viewRect = this.viewRect;
            var vrW = viewRect.w;
            var vrH = viewRect.h;
            var erW = elmsRect.w;
            var erH = elmsRect.h;
            var bw = this.barWidth;
            this.rectX = new cu.Rectangle(0, this.height - bw, this.width - bw, bw);
            this.rectY = new cu.Rectangle(this.width - bw, 0, bw, this.height - bw);

            if (elmsRect.w > viewRect.w) {
                this.isXScrollable = true;
                this.handleSizeX = vrW / erW * this.rectX.w;
                this.OFFSET_MAX_X = this.rectX.w - this.handleSizeX;
                this.offsetX = viewRect.x / (elmsRect.w + bw - viewRect.w) * this.OFFSET_MAX_X;
            } else {
                this.isXScrollable = false;
            }

            if (elmsRect.h > viewRect.h) {
                this.isYScrollable = true;
                this.handleSizeY = vrH / erH * this.rectY.h;
                this.OFFSET_MAX_Y = this.rectY.h - this.handleSizeY;
                this.offsetY = viewRect.y / (elmsRect.h + bw - viewRect.h) * this.OFFSET_MAX_Y;
            } else {
                this.isYScrollable = false;
            }
        },
        isOnHandle: function(x, y) {
            if (this.rectX && this.rectX.isIn(x, y)) {
                this.hoverType = (x > this.offsetX && (x - this.offsetX) < this.handleSizeX) ? 1 : 0;
            } else if (this.rectY && this.rectY.isIn(x, y)){
                this.hoverType = (y > this.offsetY && (y - this.offsetY) < this.handleSizeY) ? 2 : 0;
            } else {
                this.hoverType = 0;
                return false;
            }
            return this.hoverType !== 0;
        },
        draw: function(context) {
            if (!this.isVisible) {
                return;
            } else {
                context.save();
                if (this.isXScrollable && this.rectX.w > 60) {
                    this.drawScrollBarX(context);
                }
                if (this.isYScrollable && this.rectY.h > 60) {
                    this.drawScrollBarY(context);
                }
                context.restore();
            }
        },
        drawScrollBarX: function(context) {
            var rX = this.rectX;
            var rad = this.cornerRadious;
            cu.drawRoundRect(context, rX.x, rX.y, rX.w, rX.h, rad);
            //context.fillStyle = '#818181';
            context.fillStyle = '#E0E1E1';
            //context.globalAlpha = 0.2;
            context.fill();

            cu.drawRoundRect(context, rX.x + this.offsetX, rX.y, this.handleSizeX, rX.h, rad);
            var gradient = context.createLinearGradient(0, rX.y, 0, rX.b);
            gradient.addColorStop(0, '#E0E1E1');
            gradient.addColorStop(1, '#818181');
            context.fillStyle = gradient;
            //context.globalAlpha = 1;
            context.fill();
        },
        drawScrollBarY: function(context) {
            var rY = this.rectY;
            var rad = this.cornerRadious;
            cu.drawRoundRect(context, rY.x, rY.y, rY.w, rY.h, rad);
            //context.fillStyle = '#818181';
            context.fillStyle = '#E0E1E1';
            //context.globalAlpha = 0.2;
            context.fill();

            cu.drawRoundRect(context, rY.x, rY.y + this.offsetY, rY.w, this.handleSizeY, rad);
            var gradient = context.createLinearGradient(rY.x, 0, rY.r, 0);
            gradient.addColorStop(0, '#E0E1E1');
            gradient.addColorStop(1, '#818181');
            context.fillStyle = gradient;
            //context.globalAlpha = 1;
            context.fill();
        },
        onMousedown: function(e) {
            this.isPressed = true;
        },
        onMouseup: function(e) {
            this.isPressed = false;
        },
        onMouseover: function(e) {
            this.hoverPointP1 = e.p1;
        },
        onMouseout: function(e) {
            this.hoverPointP1 = null;
        },
        onMousemove: function(e) {
            if (this.isPressed) {
                if (this.hoverType === 1) {
                    var dx = e.p1.x - this.hoverPointP1.x;
                    var x = this.offsetX + dx;
                    x = x < 0 ? 0 : x;
                    x = x > this.OFFSET_MAX_X ? this.OFFSET_MAX_X : x;
                    this.offsetX = x;
                    this.viewRect.x = (this.offsetX / this.OFFSET_MAX_X) * (this.elmsRect.w + this.barWidth - this.viewRect.w);
                } else if (this.hoverType === 2){
                    var dy = e.p1.y - this.hoverPointP1.y;
                    var y = this.offsetY + dy;
                    y = y < 0 ? 0 : y;
                    y = y > this.OFFSET_MAX_Y ? this.OFFSET_MAX_Y : y;
                    this.offsetY = y;
                    this.viewRect.y = (this.offsetY / this.OFFSET_MAX_Y) * (this.elmsRect.h + this.barWidth - this.viewRect.h);
                }
                this.viewRect.update();
                this.handleOn('scroll');
            }
            this.hoverPointP1.x = this.offsetX !== 0 && this.offsetX !== this.OFFSET_MAX_X ? e.p1.x : this.hoverPointP1.x;
            this.hoverPointP1.y = this.offsetY !== 0 && this.offsetY !==  this.OFFSET_MAX_Y ? e.p1.y : this.hoverPointP1.y;
        },
        scroll: function(dx, dy) {
            var did = false;
            if (this.isXScrollable && dx) {
                did = true;
                var x = this.offsetX + dx;
                x = x < 0 ? 0 : x;
                x = x > this.OFFSET_MAX_X ? this.OFFSET_MAX_X : x;
                this.offsetX = x;
                this.viewRect.x = (this.offsetX / this.OFFSET_MAX_X) * (this.elmsRect.w + this.barWidth - this.viewRect.w);
                this.viewRect.update();
            }
            if (this.isYScrollable && dy) {
                did = true;
                var y = this.offsetY + dy;
                y = y < 0 ? 0 : y;
                y = y > this.OFFSET_MAX_Y ? this.OFFSET_MAX_Y : y;
                this.offsetY = y;
                this.viewRect.y = (this.offsetY / this.OFFSET_MAX_Y) * (this.elmsRect.h + this.barWidth - this.viewRect.h);
                this.viewRect.update();
            }
            if (did) {
                this.handleOn('scroll');
            }
        }
    });

    cu.Node = cu.createClass(cu.Element);
    cu.Node.include({
        init: function(id, imageName, name) {
            cu.Node._super.fn.init.call(this, id);

            var s = cu.Style;
            this.setStyle(s.BACKGROUND_COLOR, '#FFFFFF');
            this.setStyle(s.PADDING, 2);
            this.setStyle(s.MARGIN, 2);
            this.setStyle(s.OUTLINE_WIDTH, 0);
            this.setStyle(s.SHAPE, 'circle');
            this.setStyle(s.LABEL_DIRECTION, 'bottom');
            this.setStyle(s.LABEL_GAP, 5);

            this.icon = this.createIcon(_images[imageName]);
            this.label = this.createLabel(name);

            this.title = name ? name : null;
        },
        createIcon: function(image) {
            return new cu.NodeIcon(null, image);
        },
        createLabel: function(text) {
            return new cu.Label(null, text);
        },
        setImageName: function(name) {
            this.icon.setImage(_images[name]);
        },
        setName: function(name) {
            this.title = name;
            this.label.setText(name);
        },
        setIconCenterLocation: function(x, y) {
            switch (this.style[cu.Style.LABEL_DIRECTION]) {
                case 'top':
                    this.setCenterLocation(x, y - (this.icon.rect.cy - this.rect.cy));
                    break;
                case 'bottom':
                    this.setCenterLocation(x, y + (this.rect.cy - this.icon.rect.cy));
                    break;
                case 'left':
                    this.setCenterLocation(x - (this.icon.rect.cx - this.rect.cx), y);
                    break;
                case 'right':
                    this.setCenterLocation(x + (this.rect.cx - this.icon.rect.cx), y);
                    break;
            }
        },
        doCalcRect: function() {
            var r = this.rect;
            var margin = this.style[cu.Style.MARGIN];
            var olw = this.style[cu.Style.OUTLINE_WIDTH];
            var padding = this.style[cu.Style.PADDING];
            var gap = this.style[cu.Style.LABEL_GAP];
            var extra = padding + olw + margin;

            this.icon.calcRect();
            var rIcon = this.icon.rect;

            this.label.calcRect();
            var rLabel = this.label.rect;

            gap = rLabel.w === 0 ? 0 : gap;
            switch (this.style[cu.Style.LABEL_DIRECTION]) {
                case 'top':
                    r.w = (rIcon.w > rLabel.w ? rIcon.w : rLabel.w) + 2 * extra
                    r.h = rIcon.h + gap + rLabel.h + 2 * extra;
                    r.update();

                    this.icon.setLocation(r.cx - rIcon.w / 2, r.y + extra + rLabel.h + gap);
                    this.label.setLocation(r.cx - rLabel.w / 2, r.y + extra);
                    break;
                case 'bottom':
                    r.w = (rIcon.w > rLabel.w ? rIcon.w : rLabel.w) + 2 * extra
                    r.h = rIcon.h + gap + rLabel.h + 2 * extra;
                    r.update();

                    this.icon.setLocation(r.cx - rIcon.w / 2, r.y + extra);
                    this.label.setLocation(r.cx - rLabel.w / 2, r.y + extra + rIcon.h + gap);
                    break;
                case 'left':
                    r.w = rIcon.w + gap + rLabel.w + 2 * extra;
                    r.h = (rIcon.h > rLabel.h ? rIcon.h : rLabel.h) + 2 * extra
                    r.update();

                    this.icon.setLocation(r.x + extra + rLabel.w + gap, r.cy - rIcon.h / 2);
                    this.label.setLocation(r.x + extra, r.cy - rLabel.h / 2);
                    break;
                case 'right':
                    r.w = rIcon.w + gap + rLabel.w + 2 * extra;
                    r.h = (rIcon.h > rLabel.h ? rIcon.h : rLabel.h) + 2 * extra
                    r.update();

                    this.icon.setLocation(r.x + extra, r.cy - rIcon.h / 2);
                    this.label.setLocation(r.x + extra + rIcon.w + gap, r.cy - rLabel.h / 2);
                    break;
            }
        },
        onLocationChanged: function(dx, dy) {
            this.icon.rect.deltaLocation(dx, dy);
            this.label.rect.deltaLocation(dx, dy);
        },
        draw: function(context) {
            this.drawHighlightArea(context);
            this.icon.draw(context);
            this.label.draw(context);
        },
        drawHighlightArea: function(context) {
            var r = this.rect;
            cu.drawRoundRect(context, r.x, r.y, r.w, r.h, 3);
            context.save();
            if (this.hoverPointP2) {
                context.fillStyle = '#B9D7FC';
                context.globalAlpha = 0.8;
            } else {
                context.globalAlpha = 0;
            }
            context.fill();
            context.restore();
        },
        onMouseover: function(e) {
            cu.Node._super.fn.onMouseover.call(this, e);
            this.invalidate();
            if (this.title) {
                cu.tip.show(this.title, this.panel, this.hoverPointP1);
            }
        },
        onMouseout: function(e) {
            cu.Node._super.fn.onMouseout.call(this, e);
            if (this.title) {
                cu.tip.hide();
            }
            this.invalidate();
        },
        onMousedown: function(e) {
            cu.Node._super.fn.onMousedown.call(this, e);
            if (this.title) {
                cu.tip.hide();
            }
        }
    });

    cu.Label = cu.createClass(cu.Element);
    cu.Label.include({
        init: function(id, text) {
            cu.Label._super.fn.init.call(this, id);
            this.text = text;

            var s = cu.Style;
            this.setStyle(s.BACKGROUND_COLOR, '#e5e5e5');
            this.setStyle(s.PADDING, 4);
            this.setStyle(s.MARGIN, 0);
            this.setStyle(s.OUTLINE_WIDTH, 0);
            this.setStyle(s.OUTLINE_COLOR, '#FF0000');
            this.setStyle(s.FONT, '12px arial, tahoma, sans-serif, helvetica');
            this.setStyle(s.FONT_COLOR, '#222222');
            this.setStyle(s.CORNER_RADIOUS, 4);
            this.setStyle(s.ALPHA, 1);
            //this.doCalcRect();
        },
        setText: function(text) {
            this.text = text;
            this.isDisplayChanged = true;
        },
        doCalcRect: function() {
            if (!this.text) return;
            var r = this.rect;
            cu.g.font = this.style[cu.Style.FONT];
            var textWidth = cu.g.measureText(this.text).width;
            var fontSize = 12;// TODO 字体高度得想办法通过计算获得
            var margin = this.style[cu.Style.MARGIN];
            var olw = this.style[cu.Style.OUTLINE_WIDTH];
            var padding = this.style[cu.Style.PADDING];
            var extra = padding + olw + margin;
            r.w = textWidth + 2 * extra;
            r.h = fontSize + 2 * extra;
            r.update();
        },
        draw: function(context) {
            if (!this.text) return;
            context.save();
            this.drawFrame(context);
            this.drawText(context);
            context.restore();
        },
        drawFrame: function(context) {
            var r = this.rect;
            var margin = this.style[cu.Style.MARGIN];
            var olw = this.style[cu.Style.OUTLINE_WIDTH];
            var padding = this.style[cu.Style.PADDING];
            var rad = this.style[cu.Style.CORNER_RADIOUS];
            var x = r.x + margin + olw / 2;
            var y = r.y + margin + olw / 2;
            var w = r.w - 2 * margin - olw;
            var h = r.h - 2 * margin - olw;
            cu.drawRoundRect(context, x, y , w, h, rad);

            context.globalAlpha = this.style[cu.Style.ALPHA];
            if (olw > 0) {
                context.strokeStyle = this.style[cu.Style.OUTLINE_COLOR];
                context.stroke();
            }
            context.fillStyle = this.style[cu.Style.BACKGROUND_COLOR];
            context.fill();
        },
        drawText: function(context) {
            var r = this.rect;
            context.textAlign = "center";
            context.textBaseline = "middle";
            context.fillStyle = this.style[cu.Style.FONT_COLOR];
            context.fillText(this.text, r.cx, r.cy);
        }
    });

    cu.NodeIcon = cu.createClass(cu.Element);
    cu.NodeIcon.include({
        init: function(id, image) {
            cu.NodeIcon._super.fn.init.call(this, id);
            var s = cu.Style;
            this.setStyle(s.BACKGROUND_COLOR, '#FFFFFF');
            this.setStyle(s.PADDING, 0);
            this.setStyle(s.MARGIN, 0);
            this.setStyle(s.OUTLINE_WIDTH, 3);
            this.setStyle(s.INLINE_WIDTH, 0);
            this.setStyle(s.OUTLINE_COLOR, '#FFFFFF');
            this.setStyle(s.INLINE_COLOR, '#FFFFFF');
            this.setStyle(s.SHAPE, 'circle');
            this.setStyle(s.ALPHA, 1);
            this.image = image;
        },
        setImage: function(image) {
            this.image = image;
            this.isDisplayChanged = true;
        },
        doCalcRect: function() {
            var r = this.rect;
            var margin = this.style[cu.Style.MARGIN];
            var olw = this.style[cu.Style.OUTLINE_WIDTH];
            var ilw = this.style[cu.Style.INLINE_WIDTH];
            var padding = this.style[cu.Style.PADDING];
            var extra = padding + ilw + olw + margin;
            if (this.image) {
                var a  = this.image.width;
                var b = this.image.height;
                // var radious = Math.sqrt(a * a + b * b).toFixed(0) / 2;
                var radious = (a > b ? a : b) / 2;
                r.w = 2 * (radious + extra);
                r.h = 2 * (radious + extra);
            } else {
                r.w = r.h = 18;
            }
            r.update();
        },
        draw: function(context) {
            context.save();
            context.globalAlpha = this.style[cu.Style.ALPHA];
            this.drawOutline(context);
            this.drawImage(context);
            context.restore();
        },
        drawOutline: function(context) {
            var r = this.rect;
            var l = r.w > r.h ? r.w : r.h;
            var margin = this.style[cu.Style.MARGIN];
            var olw = this.style[cu.Style.OUTLINE_WIDTH];
            var ilw = this.style[cu.Style.INLINE_WIDTH];
            var radOutline = l / 2 - margin - olw / 2;
            var radInline = radOutline - (olw + ilw) / 2;
            var radPadding = radInline - ilw / 2;

            cu.drawCircle(context, r.cx, r.cy, radOutline);

            if (olw > 0) {
                context.strokeStyle = this.style[cu.Style.OUTLINE_COLOR];
                context.lineWidth = olw;
                context.stroke();
            }

            if (ilw > 0) {
                cu.drawCircle(context, r.cx, r.cy, radInline);
                context.strokeStyle = this.style[cu.Style.INLINE_COLOR];
                context.lineWidth = ilw;
                context.stroke();
            }

            cu.drawCircle(context, r.cx, r.cy, radPadding);
            context.fillStyle = this.style[cu.Style.BACKGROUND_COLOR];
            context.fill();

        },
        drawImage: function(context) {
            var r = this.rect;
            if (this.image) {
                var x = r.cx - this.image.width/2;
                var y = r.cy - this.image.height/2;
                cu.drawImage(context, this.image.image, x, y);
            }
        }
    });

    cu.InventoryBus = cu.createClass(cu.Element);
    cu.InventoryBus.include({
        init: function(id, gap, A, holesCount) {
            cu.InventoryBus._super.fn.init.call(this, id);
            var s = cu.Style;
            this.setStyle(s.COLOR, '#D3D3D3');
            this.setStyle(s.LINE_WIDTH, 4);

            this.isHoverable = false;
            this.isSelectable = false;
            this.isDraggable = false;

            var PI = Math.PI;
            this.holesCount = holesCount;
            this.A = null;
            var a = null;
            this.offsetY = null;
            this.updateA(A);
            this.gap = gap;
            this.isStraight = null;
        },
        updateA: function(A) {
            this.A = A;
            var a =  Math.sin(Math.PI/4)*A;
            this.offsetY = [0, a, A, a, 0, -a, -A, -a];
            this.isStraight = (this.holesCount < 5 || A < 30) ? true : false
        },
        canHover: function() {
            return false;
        },
        setStartLocation: function(x, cy) {
            this.setLocation(x, cy - this.A);
        },
        doCalcRect: function() {
            var r = this.rect;
            var lineWidth = this.style[cu.Style.LINE_WIDTH];
            var termRadious = lineWidth * 1.3;
            r.w = (this.holesCount < 1 ? 1 : this.holesCount) * this.gap + 2 * termRadious;
            r.h = this.A > 0 ? 2 * this.A : 2 * lineWidth;
            r.update();
        },
        draw: function(context) {
            var r = this.rect;
            var PI = Math.PI;
            var gap = this.gap;
            var a = PI/4/gap;
            var A = this.A;
            var lineWidth = this.style[cu.Style.LINE_WIDTH];
            var dashLen = 2 * lineWidth;
            var termRadious = lineWidth * 1.3;
            var color = this.style[cu.Style.COLOR];
            var isStraight = this.isStraight;

            context.beginPath();
            var x = r.x + termRadious;
            var y = isStraight ? r.cy : r.cy - A * Math.sin(a * (x - gap - r.x));
            context.moveTo(x, y);

            var startX = x;
            var startY = y;

            var tmpX = x;
            var tmpY = y;
            var isDash = true;
            var l = r.r-termRadious;
            for (;++x<l;) {
                if (isStraight) {
                    var len = x - tmpX;
                } else {
                    y = r.cy - A * Math.sin(a * (x - gap - r.x));
                    var len = Math.sqrt((x - tmpX) * (x - tmpX) + (y - tmpY) * (y - tmpY));
                }
                if (len > dashLen) {
                    tmpX = x;
                    tmpY = y;
                    isDash = isDash ? false : true;
                }
                if (isDash) {
                    context.lineTo(x, y);
                } else {
                    context.moveTo(x, y);
                }
            }
            var stopX = x - 1;
            var stopY = y;

            context.save();
            context.strokeStyle = color;
            context.lineWidth = lineWidth;
            context.stroke();

            cu.drawCircle(context, startX, startY, termRadious);
            context.fillStyle = color;
            context.fill();
            cu.drawCircle(context, stopX, stopY, termRadious);
            context.fill();
            context.restore();
        },
        positionElm: function(n, cuNode) {// 必须先setStartLocation再添加, n从0计数
            var r = this.rect;
            var pY = this.offsetY;
            var m = n % pY.length;
            var lineWidth = this.style[cu.Style.LINE_WIDTH];
            var termRadious = lineWidth * 1.3;
            var x = this.gap * (n+1) + termRadious;
            var y = this.isStraight ? r.cy : r.cy - pY[m];
            cuNode.setIconCenterLocation(x, y);
            cuNode.invalidate();
        }
    });

    cu.IntegTreeBus = cu.createClass(cu.Element);
    cu.IntegTreeBus.include({
        init: function(id, w, h) {
            cu.IntegTreeBus._super.fn.init.call(this, id);
            var s = cu.Style;
            this.setStyle(s.COLOR, '#656565');
            this.setStyle(s.LINE_WIDTH, 4);
            this.setStyle(s.CORNER_RADIOUS, 5);
            this.width = w;
            this.height = h;

            this.isHoverable = false;
            this.isSelectable = false;
            this.isDraggable = false;
        },
        doCalcRect: function() {
            var r = this.rect;
            r.w = this.width;
            r.h = this.height;
            r.update();
        },
        draw: function(context) {
            context.save();
            var r = this.rect;
            var lineWidth = this.style[cu.Style.LINE_WIDTH];
            var a = lineWidth / 2;
            cu.drawRoundRect(context, r.x+a, r.y+a, r.w-a, r.h-a, this.style[cu.Style.CORNER_RADIOUS]);
            context.strokeStyle = this.style[cu.Style.COLOR];
            context.lineWidth = lineWidth;
            context.stroke();
            context.restore();
        }
    });

    cu.IntegTreeNode = cu.createClass(cu.Node);
    cu.IntegTreeNode.include({
        init: function(id, imageName, name) {
            cu.IntegTreeNode._super.fn.init.apply(this, arguments);
        },
        createLabel: function(text) {
            return new cu.IntegTreeNodeLabel(null, text);
        }
    });

    cu.IntegTreeNodeLabel = cu.createClass(cu.Label);
    cu.IntegTreeNodeLabel.include({
        init: function(id, text) {
            cu.IntegTreeNodeLabel._super.fn.init.call(this, id, text);
            var s = cu.Style;
            this.setStyle(s.LABEL_MAXTEXTWIDTH, 85);
            this.setStyle(s.BACKGROUND_COLOR, '#414246');
            this.setStyle(s.PADDING, 4);
            this.setStyle(s.MARGIN, 0);
            this.setStyle(s.OUTLINE_WIDTH, 0);
            this.setStyle(s.OUTLINE_COLOR, '#FF0000');
            this.setStyle(s.FONT, '12px arial, tahoma, sans-serif, helvetica');
            this.setStyle(s.FONT_COLOR, '#FFFFFF');
            this.setStyle(s.CORNER_RADIOUS, 4);
            this.setStyle(s.ALPHA, 1);

            this.shortText = null;
        },
        doCalcRect: function() {
            var text = this.text;
            var metrics, textWidth;
            var mtw = this.style[cu.Style.LABEL_MAXTEXTWIDTH];
            for (var i=0, len=text.length;;i++) {
                this.shortText = text.substring(0, len-i) + (i>0 ? '...' : '');
                metrics = cu.g.measureText(this.shortText);
                textWidth = metrics.width;
                if (textWidth < mtw) {
                    break;
                }
            }
            var fontSize = 12;
            var padding = this.style[cu.Style.PADDING];
            var olw = this.style[cu.Style.OUTLINE_WIDTH];
            var margin = this.style[cu.Style.MARGIN];
            var extra = padding + olw + margin;
            this.rect.w = metrics.width < 60 ? 60 + extra * 2 : textWidth + extra * 2;
            this.rect.h = 2 * 12 + 2 * extra + padding;
            this.rect.update();
        },
        drawText: function(context) {
            var r = this.rect;
            var padding = this.style[cu.Style.PADDING];
            var olw = this.style[cu.Style.OUTLINE_WIDTH];
            var margin = this.style[cu.Style.MARGIN];
            var color = this.style[cu.Style.FONT_COLOR];
            var mtw = this.style[cu.Style.LABEL_MAXTEXTWIDTH];
            var extra = padding + olw + margin;
            var fontSize = 12;
            var text = this.shortText != null ? this.shortText : this.text;

            if (!this.customData['omit']) {
                var status = this.customData['status'];

                context.fillStyle = color;
                context.textAlign = "center";
                context.textBaseline = "middle";
                context.fillText(text, r.cx, r.y + extra + fontSize/2, mtw);

                var gap = 12;
                var x = r.x + extra;
                var y = r.y + extra + fontSize + padding;
                context.textAlign = "left";
                context.textBaseline = "top";
                if ('NORMAL' === status) {
                    cu.drawImage(context, 'normal_10', x, y);
                    x += gap;
                    context.fillStyle = '#00FF00';
                    context.fillText('正常运行', x, y);
                } else if ('ALARM' === status) {
                    cu.drawImage(context, 'alarm_10', x, y);
                    x += gap;
                    context.fillStyle = '#e0013a';
                    context.fillText('故障', x, y);
                } else if ('STOPCHECKED' === status) {
                    cu.drawImage(context, 'stop_check_10', x, y);
                    x += gap;
                    context.fillStyle = '#8A8A8A';
                    context.fillText('停机检修', x, y);
                }
            } else {
                var textLength = text.length;
                var re = /\d+/g;
                var arr = text.match(re);
                var x = r.x + extra;
                var y = r.cy;
                var pos = 0;

                context.textAlign = "left";
                context.textBaseline = "middle";
                if (arr) {
                    for (var i=-1, len=arr.length; ++i<len;) {
                        var start = text.indexOf(arr[i], pos);
                        var end = start + arr[i].length;
                        var strNotMatch = text.substring(pos, start);
                        var strMatch = text.substring(start, end);
                        context.save();
                        context.fillStyle = '#FFFFFF';
                        context.fillText(strNotMatch, x, y);
                        context.restore();

                        if (/\d+/.test(strMatch)) {
                            x += context.measureText(strNotMatch).width;
                            context.save();
                            context.fillStyle = '#52CCFF';
                            context.fillText(strMatch, x, y);
                            context.restore();
                        }
                        x += context.measureText(strMatch).width;
                        pos = end;
                    }
                }
                if (pos !== textLength) {
                    context.save();
                    context.fillStyle = '#FFFFFF';
                    context.fillText(text.substring(pos, textLength), x, y);
                    context.restore();
                }
            }
        }
    });

    cu.drawCircle = function(context, x, y, r) {
        context.beginPath();
        context.arc(x, y, r, 0, Math.PI*2, false);
        context.closePath();
    };

    cu.drawImage = function(context, img, x, y) {
        if (typeof(img) === 'string') {
            img = _images[img];
            if (img) {
                context.drawImage(img.image, x, y);
            }
        } else {
            context.drawImage(img, x, y);
        }
    };

    cu.drawRoundRect = function(context, x, y, w, h, r) {
        if (w < 0 || h < 0 || r < 0) {
            return;
        }
        var short = w <= h ? w : h;
        r = short >= 2 * r ? r : short/2
        var PI = Math.PI;
        context.beginPath();
        context.moveTo(x+r, y);
        context.lineTo(x+w-r, y);
        context.arc(x+w-r, y+r, r, -PI/2, 0);
        context.lineTo(x+w, y+h-r);
        context.arc(x+w-r, y+h-r, r, 0, PI/2);
        context.lineTo(x+r, y+h);
        context.arc(x+r, y+h-r, r, PI/2, PI);
        context.lineTo(x, y+r);
        context.arc(x+r, y+r, r, PI, PI*3/2);
        context.closePath();
    };
})(window);