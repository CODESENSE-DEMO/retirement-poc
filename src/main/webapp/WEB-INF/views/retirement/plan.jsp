<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../common/header.jspf" %>
<h1><spring:message code="10094"/></h1>

<form id="planForm" class="card" novalidate>
    <label><spring:message code="10099"/> <span class="unit">(세)</span>
        <input type="number" name="currentAge" value="40" min="19" max="100" required></label>
    <label><spring:message code="10013"/> <span class="unit">(세)</span>
        <input type="number" name="retirementAge" value="60" min="20" max="100" required></label>
    <label><spring:message code="10012"/> <span class="unit">(세)</span>
        <input type="number" name="lifeExpectancy" value="90" min="21" max="120" required></label>
    <label><spring:message code="10058"/> <span class="unit">(원)</span>
        <input type="number" name="monthlyRequiredAmount" value="3000000" min="0" required></label>
    <label><spring:message code="10017"/> <span class="unit">(%)</span>
        <input type="number" name="inflationRate" value="2" min="0" max="20" step="0.1" required></label>
    <label><spring:message code="10016"/> <span class="unit">(%)</span>
        <input type="number" name="expectedReturnRate" value="4" min="0" max="30" step="0.1" required></label>
    <label><spring:message code="10150"/> <span class="unit">(원)</span>
        <input type="number" name="financialAssetAmount" value="100000000" min="0" required></label>
    <label><spring:message code="10108"/> <span class="unit">(원)</span>
        <input type="number" name="publicPensionMonthlyAmount" value="1000000" min="0" required></label>
    <label><spring:message code="10109"/> <span class="unit">(원)</span>
        <input type="number" name="personalPensionMonthlyAmount" value="300000" min="0" required></label>
    <label><spring:message code="10110"/> <span class="unit">(원)</span>
        <input type="number" name="retirementPensionMonthlyAmount" value="500000" min="0" required></label>
    <label><spring:message code="10116"/> <span class="unit">(원)</span>
        <input type="number" name="realEstateCashAmount" value="0" min="0" required></label>
    <p id="formError" class="error" role="alert"></p>
    <button type="submit" class="btn"><spring:message code="10094"/></button>
</form>

<section id="planResult" class="card" hidden>
    <table class="summary">
        <tr><th><spring:message code="10053"/></th><td data-field="currentYear" data-unit="년"></td></tr>
        <tr><th><spring:message code="10054"/></th><td data-field="retirementYear" data-unit="년"></td></tr>
        <tr><th><spring:message code="10055"/></th><td data-field="lifeExpectancyYear" data-unit="년"></td></tr>
        <tr><th><spring:message code="10056"/></th><td data-field="preparationYears" data-unit="년"></td></tr>
        <tr><th><spring:message code="10057"/></th><td data-field="retirementYears" data-unit="년"></td></tr>
        <tr><th><spring:message code="10058"/></th><td data-field="monthlyRequiredAmount" data-unit="원"></td></tr>
        <tr><th><spring:message code="10042"/></th><td data-field="requiredFund" data-unit="원"></td></tr>
        <tr><th><spring:message code="10043"/></th><td data-field="preparedFund" data-unit="원"></td></tr>
        <tr><th><spring:message code="10045"/></th><td data-field="shortageFund" data-unit="원"></td></tr>
        <tr><th><spring:message code="10046"/></th><td data-field="achievementRate" data-unit="%"></td></tr>
        <tr><th><spring:message code="10047"/></th><td data-field="nonAchievementRate" data-unit="%"></td></tr>
        <tr><th><spring:message code="10106"/></th><td data-field="monthlySavingAmount" data-unit="원"></td></tr>
    </table>

    <h2><spring:message code="10105"/></h2>
    <table class="assets">
        <thead><tr><th><spring:message code="10083"/></th><th><spring:message code="10043"/></th><th></th></tr></thead>
        <tbody>
        <tr><th><spring:message code="10068"/></th><td data-field="financialAssetAmount" data-unit="원"></td>
            <td data-field="financialAssetRatio" data-unit="%" title="<spring:message code='10078'/>"></td></tr>
        <tr><th><spring:message code="10069"/></th><td data-field="publicPensionAmount" data-unit="원"></td>
            <td data-field="publicPensionRatio" data-unit="%" title="<spring:message code='10079'/>"></td></tr>
        <tr><th><spring:message code="10070"/></th><td data-field="personalPensionAmount" data-unit="원"></td>
            <td data-field="personalPensionRatio" data-unit="%" title="<spring:message code='10080'/>"></td></tr>
        <tr><th><spring:message code="10071"/></th><td data-field="retirementPensionAmount" data-unit="원"></td>
            <td data-field="retirementPensionRatio" data-unit="%" title="<spring:message code='10081'/>"></td></tr>
        <tr><th><spring:message code="10072"/></th><td data-field="realEstateCashAmount" data-unit="원"></td>
            <td data-field="realEstateRatio" data-unit="%" title="<spring:message code='10082'/>"></td></tr>
        </tbody>
    </table>
</section>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        App.bindPlanForm('<c:url value="/api/v1/retirement-plans/simulations"/>');
    });
</script>
<%@ include file="../common/footer.jspf" %>
