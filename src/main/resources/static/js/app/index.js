var main = {
    init : function () {
        var _this = this;
 
        $('#btn-save').on('click', function () {
            _this.save();
        });
 
        $("#btn-update").on('click', function () {
            _this.update();
        });

        $("#btn-delete").on('click', function () {
            _this.delete();
        });

        $("button[name='btn-del-img']").on('click', function () {
            _this.deleteImg($(this).val(), $(this).parent().parent().parent());
        });
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

        var startIndex = parseInt((page - 1) / 10) * 10 + 1;
        var href = this.makeHref(url, page - 1);

        // add previous
        if (page == 1) {
            $('#li_prev').addClass("page-item disabled");
            $('#a_prev').attr('href', '#');
            $('#a_prev').attr('tabindex', -1);
        } else {
            $('#li_prev').addClass("page-item");
            $('#a_prev').attr('href', href);
        }

        // add index 
        const page_disabled_head = "<li class=\"page-item\"><a class=\"page-link\" href=\"";
        const page_disabled_footer = "</a></li>";

        const page_active_head = "<li class=\"page-item active\"><a class=\"page-link\" href=\"#\">"
        const page_active_footer = "<span class=\"sr-only\">(current)</span></a></li>"

        for (var index = startIndex; index < startIndex + 10 && index <= maxPage; ++index) {
            if (index == page) {
                var pageHref = page_active_head + index + page_active_footer;
            } else {
                var pageHref = page_disabled_head + this.makeHref(url, index) + "\">" + index + 
                    page_disabled_footer;
            }

            $("#navigation").append(pageHref);
        }

        // add next
        href = this.makeHref(url, page + 1);
        if (page >= maxPage) {
            $('#li_next').addClass("page-item disabled");
            $('#a_next').attr('href', '#');
        } else {
            $('#li_next').addClass("page-item");
            $('#a_next').attr('href', href);
        }
    },

    makeHref : function(url, page, size = 0) {
        var ret = url + '?page=' + page;

        if (size > 0) {
            ret += '&size=' + size;
        }

        return ret;
    },

    save : function () {
        var data = new FormData();

        data.append("title", $("#title").val())
        data.append("author", $("#author").val())
        data.append("content", $("#content").val())

        const fileForms = $("#files")[0];
        for(var file of fileForms.files) {
            data.append("files", file);
        }

        $.ajax({
            type: "POST", 
            contentType: "multipart/form-data",
            url: "/api/v1/board/post",
            data: data, 
            processData: false, 
            contentType: false, 
            cache: false, 
        }).done(function() {
            alert('글이 등록되었습니다.');
            window.location.href = "/";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        })
    },

    update : function () {
        var data = {
            title : $("#title").val(),
            content : $("#content").val()
        };
        
        var id = $("#id").val();

        $.ajax({
            type : 'PUT',
            url : '/api/v1/board/'+ id,
            dataType : 'json',
            contentType : 'application/json; charset=utf-8',
            data : JSON.stringify(data)
        }).done(function () {
            alert('글이 수정되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    
    delete : function () {
        var id = $("#id").val();

        $.ajax({
            type : 'DELETE',
            url : '/api/v1/board/'+ id,
            dataType : 'json',
            contentType : 'application/json; charset=utf-8'
        }).done(function () {
            alert('글이 삭제되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    deleteImg : function (imgName, div) {
        var id = $("#id").val();

        $.ajax({
            type : 'DELETE', 
            url : '/api/v1/board/delImg/' + id + '/' + imgName.slice(5)
        }).done(function () {
            alert('이미지가 삭제되었습니다.');
            div.remove();
            // $('div').remove(this);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();

if ($("#navigation").length) {
    main.nav();
}
