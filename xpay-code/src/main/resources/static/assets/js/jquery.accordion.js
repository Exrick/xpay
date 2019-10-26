/**
 * Accordion, jQuery Plugin
 *
 * This plugin provides an accordion with cookie support.
 *
 * Copyright (c) 2011 John Snyder (snyderplace.com)
 * @license http://www.snyderplace.com/accordion/license.txt New BSD
 * @version 1.1
 */
(function ($) {
    $.fn.accordion = function (options) {

        //firewalling
        if (!this || this.length < 1) {
            return this;
        }

        initialize(this, options);

    };

    //create the initial accordion
    function initialize(obj, options) {

        //build main options before element iteration
        var opts = $.extend({}, $.fn.accordion.defaults, options);

        //store any opened default values to set cookie later
        var opened = '';

        //iterate each matched object, bind, and open/close
        obj.each(function () {
            var $this = $(this);
            saveOpts($this, opts);

            //bind it to the event
            if (opts.bind == 'mouseenter') {
                $this.bind('mouseenter', function (e) {
                    e.preventDefault();
                    toggle($this, opts);
                });
            }

            //bind it to the event
            if (opts.bind == 'mouseover') {
                $this.bind('mouseover', function (e) {
                    e.preventDefault();
                    toggle($this, opts);
                });
            }

            //bind it to the event
            if (opts.bind == 'click') {
                $this.bind('click', function (e) {
                    e.preventDefault();
                    toggle($this, opts);
                });
            }

            //bind it to the event
            if (opts.bind == 'dblclick') {
                $this.bind('dblclick', function (e) {
                    e.preventDefault();
                    toggle($this, opts);
                });
            }

            //initialize the panels
            //get the id for this element
            id = $this.attr('id');

            //if not using cookies, open defaults
            if (!useCookies(opts)) {
                //close it if not defaulted to open
                if (id != opts.defaultOpen) {
                    $this.addClass(opts.cssClose);
                    opts.loadClose($this, opts);
                } else { //its a default open, open it
                    $this.addClass(opts.cssOpen);
                    opts.loadOpen($this, opts);
                    opened = id;
                }
            } else { //can use cookies, use them now
                //has a cookie been set, this overrides default open
                if (issetCookie(opts)) {
                    if (inCookie(id, opts) === false) {
                        $this.addClass(opts.cssClose);
                        opts.loadClose($this, opts);
                    } else {
                        $this.addClass(opts.cssOpen);
                        opts.loadOpen($this, opts);
                        opened = id;
                    }
                } else { //a cookie hasn't been set open defaults
                    if (id != opts.defaultOpen) {
                        $this.addClass(opts.cssClose);
                        opts.loadClose($this, opts);
                    } else { //its a default open, open it
                        $this.addClass(opts.cssOpen);
                        opts.loadOpen($this, opts);
                        opened = id;
                    }
                }
            }
        });

        //now that the loop is done, set the cookie
        if (opened.length > 0 && useCookies(opts)) {
            setCookie(opened, opts);
        } else { //there are none open, set cookie
            setCookie('', opts);
        }

        return obj;
    }
    ;

    //load opts from object
    function loadOpts($this) {
        return $this.data('accordion-opts');
    }

    //save opts into object
    function saveOpts($this, opts) {
        return $this.data('accordion-opts', opts);
    }

    //hides a accordion panel
    function close(opts) {
        opened = $(document).find('.' + opts.cssOpen);
        $.each(opened, function () {
            //give the proper class to the linked element
            $(this).addClass(opts.cssClose).removeClass(opts.cssOpen);
            opts.animateClose($(this), opts);
        });
    }

    //opens a accordion panel
    function open($this, opts) {
        close(opts);
        //give the proper class to the linked element
        $this.removeClass(opts.cssClose).addClass(opts.cssOpen);

        //open the element
        opts.animateOpen($this, opts);

        //do cookies if plugin available
        if (useCookies(opts)) {
            // split the cookieOpen string by ","
            id = $this.attr('id');
            setCookie(id, opts);
        }
    }

    //toggle a accordion on an event
    function toggle($this, opts) {
        // close the only open item
        if ($this.hasClass(opts.cssOpen))
        {
            close(opts);
            //do cookies if plugin available
            if (useCookies(opts)) {
                // split the cookieOpen string by ","
                setCookie('', opts);
            }
            return false;
        }
        close(opts);
        //open a closed element
        open($this, opts);
        return false;
    }

    //use cookies?
    function useCookies(opts) {
        //return false if cookie plugin not present or if a cookie name is not provided
        if (!$.cookie || opts.cookieName == '') {
            return false;
        }

        //we can use cookies
        return true;
    }

    //set a cookie
    function setCookie(value, opts)
    {
        //can use the cookie plugin
        if (!useCookies(opts)) { //no, quit here
            return false;
        }

        //cookie plugin is available, lets set the cookie
        $.cookie(opts.cookieName, value, opts.cookieOptions);
    }

    //check if a accordion is in the cookie
    function inCookie(value, opts)
    {
        //can use the cookie plugin
        if (!useCookies(opts)) {
            return false;
        }

        //if its not there we don't need to remove from it
        if (!issetCookie(opts)) { //quit here, don't have a cookie
            return false;
        }

        //unescape it
        cookie = unescape($.cookie(opts.cookieName));

        //is this value in the cookie arrray
        if (cookie != value) { //no, quit here
            return false;
        }

        return true;
    }

    //check if a cookie is set
    function issetCookie(opts)
    {
        //can we use the cookie plugin
        if (!useCookies(opts)) { //no, quit here
            return false;
        }

        //is the cookie set
        if ($.cookie(opts.cookieName) == null) { //no, quit here
            return false;
        }

        return true;
    }

    // settings
    $.fn.accordion.defaults = {
        cssClose: 'accordion-close', //class you want to assign to a closed accordion header
        cssOpen: 'accordion-open', //class you want to assign an opened accordion header
        cookieName: 'accordion', //name of the cookie you want to set for this accordion
        cookieOptions: {//cookie options, see cookie plugin for details
            path: '/',
            expires: 7,
            domain: '',
            secure: ''
        },
        defaultOpen: '', //id that you want opened by default
        speed: 'slow', //speed of the slide effect
        bind: 'click', //event to bind to, supports click, dblclick, mouseover and mouseenter
        animateOpen: function (elem, opts) { //replace the standard slideDown with custom function
            elem.next().stop(true, true).slideDown(opts.speed);
        },
        animateClose: function (elem, opts) { //replace the standard slideUp with custom function
            elem.next().stop(true, true).slideUp(opts.speed);
        },
        loadOpen: function (elem, opts) { //replace the default open state with custom function
            elem.next().show();
        },
        loadClose: function (elem, opts) { //replace the default close state with custom function
            elem.next().hide();
        }
    };
})(jQuery);