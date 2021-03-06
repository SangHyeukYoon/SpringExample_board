var main = {
    init : function () {
        var _this = this;
 
        $("#btn-update").on('click', function () {
            _this.update();
        });

        $("#btn-delete").on('click', function () {
            _this.delete();
        });

        $("button[name='btn-del-img']").on('click', function () {
            _this.deleteImg($(this).val(), $(this).parent());
        });

        textContent = $("#content").val();
        textContent = textContent.replaceAll("<br>", "\r\n");
        $("#content").val(textContent);
    },

    update : function () {
        textContent = $("#content").val();
        textContent = textContent.replaceAll(/(\n|\r\n)/g, "<br>");

        var data = {
            title : $("#title").val(),
            content : textContent
        };
        
        var id = $("#id").val();

        $.ajax({
            type : 'PUT',
            url : '/api/v1/board/'+ id,
            dataType : 'json',
            contentType : 'application/json; charset=utf-8',
            data : JSON.stringify(data),
            async: false,
            statusCode: {
                401: function (response) {
                    alert('권한이 없습니다.');
            }}
        }).done(function () {
            alert('글이 수정되었습니다.');
            window.location.href = '/board/read/' + id;
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
            contentType : 'application/json; charset=utf-8', 
            async: false
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
            url : '/api/v1/board/delImg/' + id + '/' + imgName.slice(5), 
            async: false
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
