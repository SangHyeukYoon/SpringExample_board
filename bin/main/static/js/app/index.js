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

        $(".btn-delete-img").on('click', function () {
            _this.deleteImg($(this).val());
        });
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

    deleteImg : function (imgName) {
        var id = $("#id").val();

        $.ajax({
            type : 'DELETE', 
            url : '/api/v1/board/delImg/' + id + '/' + imgName.slice(5)
        }).done(function () {
            alert('이미지가 삭제되었습니다.');
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();