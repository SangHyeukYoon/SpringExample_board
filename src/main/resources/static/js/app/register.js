var main = {
    init : function () {
        var _this = this;

        $("#btn-register").on('click', function () {
            _this.registerUser();
        });
    },

    registerUser : function () {
        var data = {
            nickname : $("#nickname").val()
        };

        $.ajax({
            type: "POST",
            url: "/register",
            data: JSON.stringify(data),
            dataType : 'json',
            contentType : 'application/json; charset=utf-8',
            async: false,
        }).done(function() {
            alert('가입되었습니다.');
            window.location.href = "/";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();
