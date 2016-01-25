
(function($CQ, CQ) {
    "use strict";
    CQ.soco.Poll = CQ.soco.Poll || {};
    CQ.soco.Poll = function(pollElement) {
        if (arguments.length == 0) return;
        var that = this;
        this.ele = pollElement;
        this.form = this.ele.find("form");
        this.form.find("input:button").bind("click",function(){
            var response = that.getResponse();
            if(that.validate(response)) {
                that.submit(response);
            }
        });
    };
    CQ.soco.Poll.prototype.OTHER_OPTION = "soco.other";
    CQ.soco.Poll.prototype.POLL_SUBMIT_EVENT = "soco.poll.submit";
    CQ.soco.Poll.prototype.getResponse = function(){
        var selection = this.form.find("input[name=response]:checked").val();
            if(selection === this.OTHER_OPTION) {
                return this.form.find("input.other-text").val();
            }
        return selection;
    };
    CQ.soco.Poll.prototype.validate = function(response){
        if(response === undefined || response == "") {
            this.ele.find(".error").show();
            return false;
        }
        else {
            this.ele.find(".error").hide();
            return true;
        }
    }
    CQ.soco.Poll.prototype.submit = function(response){
            var that = this;
            var data = this.form.find("input:not(input[name=response])").serialize();
            data += "&" + encodeURIComponent(CQ.soco.TEMPLATE_PARAMNAME) + "=" + encodeURIComponent('poll')
                + "&" + encodeURIComponent("response") + "=" + encodeURIComponent(response);
            $CQ.ajax(this.form.attr('action'), {
                    data: data,
                    success: function(data, status, jqxhr) {
                        if (jqxhr.status === 201) {
                            CQ.soco.filterHTMLFragment(data, function(node) {
                                that.ele.replaceWith(node);
                                that = undefined;
                            });
                        }
                    },
                    fail: function(jqxhr, status) {
                        throw status;
                    },
                    type: "POST"
                });
    };
})($CQ, CQ);
