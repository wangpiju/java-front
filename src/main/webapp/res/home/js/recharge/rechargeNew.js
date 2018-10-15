
$(function () {

    var code_v;
    var message_v;

    $.ajax({
        type: "GET",
        async: false,
        url: document.location.origin + "/api/proxy/rechargeEntrance",
        dataType: "json",
        data: {rechargeWay:$('#rechargeWay').val()},  //请求参数
        success: function(datas){
            var json = eval(datas); //数组
            $.each(json, function (index) {
                if(index == 'code'){
                    code_v = json[index];
                }

                if(code_v != null && code_v == 1) {
                    if (index == 'data') {
                        //收款银行数据ID
                        var receiveBankId = json[index].receiveBankId;
                        document.getElementById('receiveBankId').value = receiveBankId;

                        var waytype = json[index].waytype;
                        if(waytype == 1 || waytype == 2) {
                            //收款银行名称
                            var receiveBankName = json[index].receiveBankName;
                            $('#receiveBankName').html(receiveBankName);
                            //收款银行卡号
                            var receiveCard = json[index].receiveCard;
                            $('#receiveCard').html(receiveCard);
                            //收款银行地址
                            var receiveAddress = json[index].receiveAddress;
                            $('#receiveAddress').html(receiveAddress);
                            //收款银行开户人姓名
                            var receiveNiceName = json[index].receiveNiceName;
                            $('#receiveNiceName').html(receiveNiceName);

                        }

                        //二维码URL
                        var QRCodeUrl = json[index].QRCodeUrl;
                        //alert(QRCodeUrl);
                        document.getElementById("QRCodeUrl").src = document.location.origin + QRCodeUrl;

                        var attsecond = json[index].attsecond;
                        $('#attsecond').html(attsecond);
                    }
                }

                if(code_v != null && code_v == 0){
                    if (index == 'data') {
                        message_v = json[index].message;
                    }
                }
            });
        }

    });

    if(code_v != null && code_v == 0) {
        $.alert(message_v);
    }

});

function setPayRequest () {

    var successFlag = $('#successFlag').val();

    if(successFlag == 1){
        $.alert("请勿重复提交申请！");
        return;
    }

    var code_v;
    var message_v;

    $.ajax({
        type: "POST",
        async: false,
        url: document.location.origin + "/api/proxy/setPayRequest",
        dataType: "json",
        data: {rechargeWay:$('#rechargeWay').val(), chargeamount:$('#chargeamount').val(), receiveBankId:$('#receiveBankId').val(), niceName:$('#niceName').val(), checkCode:$('#checkCode').val()},
        success: function(datas){
            var json = eval(datas); //数组
            $.each(json, function (index) {
                if(index == 'code'){
                    code_v = json[index];
                }
                if(index == 'data'){
                    var message = json[index].message;
                    message_v = message;
                }
            });
        }

    });

    if(code_v != null){
        $.alert(message_v);
        if(code_v == 1){
            $("#setPayRequest").css("color","#D0D0D0");
            document.getElementById('successFlag').value = 1;
        }
    }

}
