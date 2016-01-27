var MyJCRService = function () {};

MyJCRService.prototype.ins = function (path, object, done_callback, fail_callback) {
    $.ajax({
        url: "/bin/JCRServiceServlet",
        data : "json=" + object,
    })
    .done(done_callback)
    .fail(fail_callback);
};

var srv = new MyJCRService();
srv.ins(
    "path/to/node",
    {
        query : "INS",
		target: document.getElementById('ins_t').value,
		param : [
		  [ "chiave", "valore" ],
		],
    },
    function () {
    
    },
    function () {
    
    }
);