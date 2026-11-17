<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../common/header.jspf" %>
<section class="intro">
    <h1><spring:message code="10165"/></h1>
    <p class="lead"><spring:message code="10166"/></p>
    <p class="notice"><spring:message code="10167"/></p>
    <button type="button" class="btn" id="startPlan"><spring:message code="10094"/></button>
</section>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        document.getElementById('startPlan').addEventListener('click', function () {
            if (App.isArLinked()) {
                location.href = '<c:url value="/retirement/plan"/>';
                return;
            }
            App.confirm(function () {
                location.href = '<c:url value="/ar-link/complete"/>';
            });
        });
    });
</script>
<%@ include file="../common/footer.jspf" %>
