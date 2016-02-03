/**
 * Created by talas on 7/28/15.
 */
//((function () {
console.log("JarvisStore lib is loaded...");
//var listIssues = jQuery("#btn-jarvis-list-researches");
//listIssues.on("click", function () {
//    var researchIssueList = jQuery("#jarvis-research-list-ctrl");
//    var currentState = researchIssueList.attr("data-jarvis-list-state");
//    if (currentState === "collapse") {
//        jQuery(researchIssueList).attr("data-jarvis-list-state", "expand");
//    }
//    else {
//        jQuery(researchIssueList).attr("data-jarvis-list-state", "collapse");//, "jarvis-list-state", "collapse");
//    }
//    console.log(currentState);
//});

//var jarvisMenu = jQuery("#jarvis-research-list-ctrl");
//jarvisMenu.on("mouseleave", function () {
//    setTimeout(function () {
//        var researchIssueList = jQuery("#jarvis-research-list-ctrl");
//        if (!jQuery("#btn-jarvis-list-researches").is(":hover")) {
//            researchIssueList.attr("data-jarvis-list-state", "collapse");
//        }
//    }, 500);
//});
//})).call(this);

((function () {

    var JarvisStore, __bind = function (fn, me) {
            return function () {
                return fn.apply(me, arguments)
            }
        },
        __hasProp = Object.prototype.hasOwnProperty,
        __extends = function (child, parent) {
            function ctor() {
                this.constructor = child
            }

            ctor.prototype = parent.prototype;
            child.prototype = new ctor;
            child.__super__ = parent.prototype;

            for (var key in parent) {
                if (__hasProp.call(parent, key)) {
                    (child[key] = parent[key]);
                }
            }
            return child;
        },
        __indexOf = [].indexOf || function (item) {
                for (var i = 0, l = this.length; i < l; i++) {
                    if (i in this && this[i] === item)return i
                }
                return -1
            };

    Annotator.Plugin.JarvisStore = JarvisStore = function (_super) {

        var _t;
        __extends(JarvisStore, _super);

        //JarvisStore.prototype.events = {
        //    annotationCreated: "annotationCreated",
        //    annotationDeleted: "annotationDeleted",
        //    annotationUpdated: "annotationUpdated",
        //    beforeAnnotationCreated: "setupResearchFields"
        //};
        JarvisStore.prototype.options = {
            hostURL: "some host url",
            basePath: "basePath",
            researchSession: "researchSession",
            annotationData: {},
            emulateHTTP: false,
            loadFromSearch: false,
            prefix: "/store"
        };

        function JarvisStore(element, options) {

            var handlers, event, handler;
            this._onError = __bind(this._onError, this);
            this._onLoadAnnotationsFromSearch = __bind(this._onLoadAnnotationsFromSearch, this);
            this._onLoadAnnotations = __bind(this._onLoadAnnotations, this);
            this._getAnnotations = __bind(this._getAnnotations, this);
            JarvisStore.__super__.constructor.apply(this, arguments);
            handlers = {
                annotationCreated: "onAnnotationCreated",
                annotationDeleted: "onAnnotationDeleted",
                annotationUpdated: "onAnnotationUpdated",
                beforeAnnotationCreated: "onBeforeAnnotationCreated"
            };
            this.annotations = [];
            for (event in handlers) {
                if (!__hasProp.call(handlers, event)) continue;
                handler = handlers[event];
                if (typeof this.options[handler] === "function") {
                    this.on(event, jQuery.proxy(this.options, handler));
                }
            }
        }

        JarvisStore.prototype.pluginInit = function () {
            console.log("Initialized with options");
            console.log(this.options);
            if (!Annotator.supported()) {
                return
            }
            console.log("Store plugin initialized...");
            //if (this.annotator.plugins.Auth) {
            //    return this.annotator.plugins.Auth.withToken(this._getAnnotations)
            //} else {
            //    return this._getAnnotations()
            //}
        };

        JarvisStore.prototype._getAnnotations = function () {
            if (this.options.loadFromSearch) {
                return this.loadAnnotationsFromSearch(this.options.loadFromSearch)
            } else {
                return this.loadAnnotations()
            }
        };
        JarvisStore.prototype.setupResearchFields = function (annotation) {
            if (annotation) {
                annotation.researchSession = this.options.researchSession;
            }
        };
        JarvisStore.prototype.annotationCreated = function (annotation) {
            var _this = this;
            if (__indexOf.call(this.annotations, annotation) < 0) {
                this.registerAnnotation(annotation);
                console.log("Calling error notification...");
                self.port.emit("onAnnotationCreated", annotation);
                this._onError("create", 401);
            } else {
                return this.updateAnnotation(annotation, {})
            }
        };
        JarvisStore.prototype.annotationUpdated = function (annotation) {
            var _this = this;
            if (__indexOf.call(this.annotations, annotation) >= 0) {
                //var opts = {
                //    url: this._makeUri("/sessions/" + annotation.researchSession + "/capture/" + annotation.id),
                //    content: JSON.stringify(annotation)
                //};
                //
                //return this._doRequest(opts, function (response) {
                //    if (response.status !== 200) {
                //        _this._onError("update", response.status);
                //        return;
                //    }
                //    return _this.updateAnnotation(annotation, response.json);
                //}, "PUT");

                //return this._apiRequest("update", annotation, function (data) {
                //    return _this.updateAnnotation(annotation, data)
                //})
            }
        };
        JarvisStore.prototype.annotationDeleted = function (annotation) {
            var _this = this;
            if (__indexOf.call(this.annotations, annotation) >= 0) {

                //var opts = {
                //    url: this._makeUri("/sessions/" + annotation.researchSession + "/capture/" + annotation.id)
                //};
                //this._doRequest(opts, function (response) {
                //    if (response.status !== 200) {
                //        _this._onError("destroy", response.status);
                //        return;
                //    }
                //    return _this.unregisterAnnotation(annotation);
                //}, "DELETE");

                //return this._apiRequest("destroy", annotation, function () {
                //    return _this.unregisterAnnotation(annotation)
                //})
            }
        };
        JarvisStore.prototype.registerAnnotation = function (annotation) {
            return this.annotations.push(annotation)
        };
        JarvisStore.prototype.unregisterAnnotation = function (annotation) {
            return this.annotations.splice(this.annotations.indexOf(annotation), 1)
        };
        JarvisStore.prototype.updateAnnotation = function (annotation, data) {
            if (__indexOf.call(this.annotations, annotation) < 0) {
                console.error(Annotator._t("Trying to update unregistered annotation!"))
            } else {
                $.extend(annotation, data)
            }
            return $(annotation.highlights).data("annotation", annotation)
        };
        JarvisStore.prototype.loadAnnotations = function () {
            var opts = {
                url: this._makeUri("/sessions/captures")
            };
            return this._doRequest(options, this._onLoadAnnotations, "GET");
            //return this._apiRequest("read", null, this._onLoadAnnotations)
        };
        JarvisStore.prototype._onLoadAnnotations = function (response) {

            if (response.status !== 200) {
                this._onError("read", response.status);
                return;
            }

            var a, annotation, annotationMap, newData, _k, _l, _len2, _len3, _ref3, data;
            data = response.json;

            if (data == null) {
                data = []
            }
            annotationMap = {};
            _ref3 = this.annotations;
            for (_k = 0, _len2 = _ref3.length; _k < _len2; _k++) {
                a = _ref3[_k];
                annotationMap[a.id] = a
            }
            newData = [];
            for (_l = 0, _len3 = data.length; _l < _len3; _l++) {
                a = data[_l];
                if (annotationMap[a.id]) {
                    annotation = annotationMap[a.id];
                    this.updateAnnotation(annotation, a)
                } else {
                    newData.push(a)
                }
            }
            this.annotations = this.annotations.concat(newData);
            return this.annotator.loadAnnotations(newData.slice())
        };
        JarvisStore.prototype.loadAnnotationsFromSearch = function (searchOptions) {
            return this._apiRequest("search", searchOptions, this._onLoadAnnotationsFromSearch)
        };
        JarvisStore.prototype._onLoadAnnotationsFromSearch = function (data) {
            if (data == null) {
                data = {}
            }
            return this._onLoadAnnotations(data.rows || [])
        };
        JarvisStore.prototype.dumpAnnotations = function () {
            var ann, _k, _len2, _ref3, _results;
            _ref3 = this.annotations;
            _results = [];
            for (_k = 0, _len2 = _ref3.length; _k < _len2; _k++) {
                ann = _ref3[_k];
                _results.push(JSON.parse(this._dataFor(ann)))
            }
            return _results
        };

        JarvisStore.prototype._urlFor = function (action, id) {
            var url;
            url = this.options.prefix != null ? this.options.prefix : "";
            url += this.options.urls[action];
            url = url.replace(/\/:id/, id != null ? "/" + id : "");
            url = url.replace(/:id/, id != null ? id : "");
            return url
        };
        JarvisStore.prototype._methodFor = function (action) {
            var table;
            table = {create: "POST", read: "GET", update: "PUT", destroy: "DELETE", search: "GET"};
            return table[action]
        };
        JarvisStore.prototype._dataFor = function (annotation) {
            var data, highlights;
            highlights = annotation.highlights;
            delete annotation.highlights;
            $.extend(annotation, this.options.annotationData);
            data = JSON.stringify(annotation);
            if (highlights) {
                annotation.highlights = highlights
            }
            return data
        };
        JarvisStore.prototype._onError = function (act, status) {
            var action, message;
            var response = {};
            response._action = act;
            response.status = status;

            action = response._action;
            message = Annotator._t("Sorry we could not ") + action + Annotator._t(" this annotation");
            if (response._action === "search") {
                message = Annotator._t("Sorry we could not search the store for annotations")
            } else if (response._action === "read" && !response._id) {
                message = Annotator._t("Sorry we could not ") + action + Annotator._t(" the annotations from the store")
            }
            switch (response.status) {
                case 401:
                    message = Annotator._t("Sorry you are not allowed to ") + action + Annotator._t(" this annotation");
                    break;
                case 404:
                    message = Annotator._t("Sorry we could not connect to the annotations store");
                    break;
                case 500:
                    message = Annotator._t("Sorry something went wrong with the annotation store")
            }
            console.log(message);
            Annotator.showNotification(message, Annotator.Notification.ERROR);
            return console.error(Annotator._t("API request failed:") + (" '" + response.status + "'"))
        };
        return JarvisStore;
    }(Annotator.Plugin);
})).call(this);

