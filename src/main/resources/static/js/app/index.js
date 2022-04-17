var main = {
    init : function () {
        var _this = this;
        this.nav();
    },

    nav : function() {
        var maxPage = $('#maxPage').val();
        var url = window.location.origin;
        var param = window.location.href;

        var pageStrStartIndex = param.indexOf('page=');
        var pageStrEndIndex = param.indexOf('&');

        if (pageStrStartIndex < 0) {
            var page = 1;
        } else {
            pageStrStartIndex += 5;

            if (pageStrEndIndex < 0) {
                pageStrEndIndex = param.length;
            }
            var page = Number(param.substring(pageStrStartIndex, pageStrEndIndex))
        }

        var startIndex = parseInt((page - 1) / 5) * 5 + 1;
        var href = this.makeHref(url, page - 1);

        // add previous
        if (page == 1) {
            $('#prev-a').attr('href', '#');
        } else {
            $('#prev-a').attr('href', href);
        }

        // add index 
        const page_disabled_head = "<button class=\"navBtn\"><a href=\">"
        const page_disabled_footer = "</a></button>"

        const page_active_head = "<button class=\"navBtn-active\" disabled><a href=\"#\">"
        const page_active_footer = "</a></button>"

        for (var index = startIndex; index < startIndex + 5 && index <= maxPage; ++index) {
            if (index == page) {
                var pageHref = page_active_head + index + page_active_footer;
            } else {
                var pageHref = page_disabled_head + this.makeHref(url, index) + "\">" + index + 
                    page_disabled_footer;
            }

            $(".nav-insert").append(pageHref);
        }

        // add next
        href = this.makeHref(url, page + 1);
        if (page >= maxPage) {
            $('#next-a').attr('href', '#');
        } else {
            $('#next-a').attr('href', href);
        }
    },

    makeHref : function(url, page, size = 0) {
        var ret = url + '?page=' + page;

        if (size > 0) {
            ret += '&size=' + size;
        }

        return ret;
    },
};

main.init();
