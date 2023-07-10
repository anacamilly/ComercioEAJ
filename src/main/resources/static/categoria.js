function toggleCategoriaFilter(selectElement) {
    var categoriaFilter = document.getElementById('categoriaFilter');
    categoriaFilter.style.display = (selectElement.value === 'categoria' || selectElement.value === 'nomeCategoria') ? 'block' : 'none';
}