/* 화면 공통 스크립트: 확인 모달, AR 연동 상태, 은퇴설계 폼 */
var App = (function () {
    var AR_LINKED_KEY = 'arLinked';

    // ponytail: UX 분기용 클라이언트 플래그. 서버 인가가 필요한 기능(정보저장 등)이 생기면 JWT 로그인으로 교체.
    function storage() {
        try { return window.sessionStorage; } catch (e) { return null; }
    }

    function isArLinked() {
        var s = storage();
        return !!s && s.getItem(AR_LINKED_KEY) === 'Y';
    }

    function markArLinked() {
        var s = storage();
        if (s) s.setItem(AR_LINKED_KEY, 'Y');
    }

    function confirm(onYes) {
        var modal = document.getElementById('confirmModal');
        modal.hidden = false;
        modal.querySelector('[data-confirm="yes"]').focus();
        modal.onclick = function (e) {
            var answer = e.target.getAttribute('data-confirm');
            if (!answer) return;
            modal.hidden = true;
            if (answer === 'yes') onYes();
        };
    }

    function format(value, unit) {
        var text = (unit === '원') ? Number(value).toLocaleString('ko-KR') : String(value);
        return text + ' ' + unit;
    }

    function bindPlanForm(apiUrl) {
        var form = document.getElementById('planForm');
        var errorBox = document.getElementById('formError');
        var result = document.getElementById('planResult');

        form.addEventListener('submit', function (e) {
            e.preventDefault();
            errorBox.textContent = '';
            var body = {};
            Array.prototype.forEach.call(form.elements, function (el) {
                if (el.name) body[el.name] = el.value === '' ? null : Number(el.value);
            });

            fetch(apiUrl, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(body)
            }).then(function (res) { return res.json(); })
              .then(function (api) {
                  if (!api.success) {
                      var reasons = (api.errors || []).map(function (err) { return err.reason; });
                      errorBox.textContent = reasons.length ? reasons.join(' / ') : api.message;
                      result.hidden = true;
                      return;
                  }
                  result.querySelectorAll('[data-field]').forEach(function (cell) {
                      cell.textContent = format(api.data[cell.getAttribute('data-field')], cell.getAttribute('data-unit'));
                  });
                  result.hidden = false;
              })
              .catch(function () { errorBox.textContent = '요청 처리 중 오류가 발생했습니다.'; });
        });
    }

    return { isArLinked: isArLinked, markArLinked: markArLinked, confirm: confirm, bindPlanForm: bindPlanForm };
})();
