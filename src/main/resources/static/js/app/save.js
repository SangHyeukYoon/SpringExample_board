var main = {
    init : function () {
        var _this = this;
 
        $('#btn-save').on('click', function () {
            _this.save();
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
            async: false
        }).done(function() {
            alert('글이 등록되었습니다.');
            window.location.href = "/";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();
