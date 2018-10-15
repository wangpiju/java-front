<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>上传文件</title>
    <meta http-equiv="Content-Type" content="multipart/form-data; charset=utf-8" />
    <link rel="stylesheet" href="<c:url value='/res/home/css/wk-recharge.css?ver=${VIEW_VERSION}'/>"/>
    <link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
    <script type="text/javascript">

        function saveData(){

            //var code_v;
            //var message_v;

            var form = new FormData(document.getElementById("fileForm"));
            $.ajax({
                url: document.location.origin + "/upload/setFile",
                type:"post",
                data:form,
                async: false,
                cache: false,
                processData:false,
                contentType:false,
                dataType: "json",
                success:function(data){
                    //alert(data);
                    //console.log("over..");
                    var qrcodeurl = data.qrcodeurl;
                    //alert(qrcodeurl);
                    document.getElementById('qrcodeurl').innerHTML = qrcodeurl;
                },
                error:function(e){
                    alert("上传文件异常！");
                    //console.log("2222222..");
                }
            });
            return false;
        }

    </script>
</head>
<body>
<form id="fileForm">
    <table class="formtable">

        <tr>

            <td>&nbsp;</td>

            <td> &nbsp;</td>

        </tr>

        <tr>

            <td class="input-title">&nbsp;&nbsp;二维码上传：</td>

            <td>
                <input id="sourceFile" type="file" name="sourceFile" multiple="true">
            </td>

        </tr>

        <tr>

            <td>&nbsp;</td>

            <td> &nbsp;</td>

        </tr>


        <tr>

            <td class="input-title">&nbsp;&nbsp;二维码文件名：</td>

            <td>
                <span id="qrcodeurl"></span>
            </td>

        </tr>

        <tr>

            <td>&nbsp;</td>

            <td> &nbsp;</td>

        </tr>


    </table>

    <div style="text-align: left; padding: 5px;">

        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class="btn" type="button" onclick="saveData()" value="提交"/>

    </div>
</form>
</body>
</html>