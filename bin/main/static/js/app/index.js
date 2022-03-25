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

        $("#btn-register").on('click', function () {
            _this.register();
        });

        $("#btn-login").on('click', function () {
            _this.login();
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
            enctype: "multipart/form-data", 
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

    register : function () {
        var data = {
            username : $("#username").val(),
            password : $("#password").val(),
        };

        $.ajax({
            type : 'POST',
            url : '/api/v1/register',
            // dataType : 'text',
            contentType : 'application/json; charset=utf-8',
            data : JSON.stringify(data)
        }).done(function () {
            alert('가입되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            // alert('이미 존재하는 아이디입니다.');
            // err = JSON.stringify(error);
            alert(error.responseJSON.message);
        });
    }, 

    login : function () {
        var data = {
            username : $("#username").val(),
            password : $("#password").val()
        };

        $.ajax({
            type : 'POST',
            url : '/api/v1/login',
            // dataType : 'text',
            // contentType : 'application/x-www-form-urlencoded; charset=utf-8'
            data : data
        }).done(function (response) {
            // alert(JSON.stringify(response))
            alert('로그인 성공')
            window.location.href = '/'
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();