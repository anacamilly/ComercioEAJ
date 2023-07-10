document.addEventListener('DOMContentLoaded', function() {
    const precoElements = document.querySelectorAll('.fw-bold > span');
    precoElements.forEach(function(element) {
        const precoValue = parseFloat(element.textContent);
        const formattedPreco = precoValue.toFixed(2).replace('.', ',');
        element.textContent = 'R$ ' + formattedPreco;
    });
});