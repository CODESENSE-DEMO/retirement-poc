<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../common/header.jspf" %>
<section class="intro">
    <h1><spring:message code="10000"/></h1>
    <p class="lead"><spring:message code="10001"/></p>
    <p><spring:message code="10002"/></p>
    <button type="button" class="btn" id="startActionB"><spring:message code="10003"/></button>
</section>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        document.getElementById('startActionB').addEventListener('click', function () {
            App.markArLinked();
            location.href = '<c:url value="/retirement/plan"/>';
        });
    });
</script>
<%@ include file="../common/footer.jspf" %>
