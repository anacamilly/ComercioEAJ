document.getElementById('imagem').addEventListener('change', function(event) {
    var input = event.target;
    var reader = new FileReader();

    reader.onload = function() {
        var img = document.getElementById('preview');
        img.src = reader.result;

        // Exibir o modal do cropper
        var cropperModal = new bootstrap.Modal(document.getElementById('cropper-modal'));
        cropperModal.show();

        // Inicializar o Cropper.js
        var cropper = new Cropper(img, {
            aspectRatio: 1, // Proporção de aspecto desejada (opcional)
            viewMode: 2, // Modo de visualização (opcional)
            autoCropArea: 0.8, // Área de corte automático (opcional)
            responsive: true, // Tornar a área de corte responsiva
            cropBoxResizable: true // Permitir redimensionamento da área de corte
        });

        // Ajustar a largura e altura do quadro de corte
        var cropBoxData = cropper.getCropBoxData();
        cropBoxData.width = 300; // Aumentar a largura em 100 pixels (ajuste conforme necessário)
        cropBoxData.height = 300; // Aumentar a altura em 100 pixels (ajuste conforme necessário)
        cropper.setCropBoxData(cropBoxData);

        // Atualizar a imagem cortada quando o botão for clicado
        document.getElementById('btn-cortar').addEventListener('click', function() {
            var canvas = cropper.getCroppedCanvas();
            var croppedImage = canvas.toDataURL();

            // Crie um campo de entrada oculto para armazenar a imagem cortada
            var hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = 'croppedImage';
            hiddenInput.value = croppedImage;
            document.getElementById('imagem').parentNode.appendChild(hiddenInput);

            // Exiba a imagem cortada (opcional)
            var croppedPreview = document.createElement('img');
            croppedPreview.src = croppedImage;
            document.getElementById('preview').parentNode.appendChild(croppedPreview);

            // Feche o modal do cropper
            cropperModal.hide();
        });
    };

    reader.readAsDataURL(input.files[0]);
});