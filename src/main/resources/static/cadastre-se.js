$(document).ready(function() {
    $('.telefone').inputmask('+55 (99) 9999-9999');
});

function mostrarCamposAdicionais() {
    var vendSim = document.getElementById("vendSim");
    var camposAdicionais = document.getElementById("camposAdicionais");

    if (vendSim.checked) {
        camposAdicionais.style.display = "block";
    } else {
        camposAdicionais.style.display = "none";
    }
}

function validarSenhas() {
    var senha = document.getElementById("senha").value;
    var senhaConfirmacao = document.getElementById("senhaConfirmacao").value;

    if (senha !== senhaConfirmacao) {
        alert("As senhas devem ser iguais!");
        return false;
    }

    return true;
}

window.addEventListener("DOMContentLoaded", function () {
    var imagemInput = document.getElementById("imagem");
    var imagemPreview = document.getElementById("imagemPreview");
    var btnAbrirRecorte = document.getElementById("btnAbrirRecorte");

    imagemInput.addEventListener("change", function () {
        var reader = new FileReader();
        reader.onload = function (e) {
            imagemPreview.src = e.target.result;
            imagemPreview.style.display = "block";
            btnAbrirRecorte.style.display = "block";
        };
        reader.readAsDataURL(this.files[0]);
    });

    btnAbrirRecorte.addEventListener("click", function () {
        var imagemOriginal = document.getElementById("imagemPreview");
        var cropper = new Cropper(imagemOriginal, {
            aspectRatio: 1,
            crop: function (event) {
                var coordenadasCorteInput = document.getElementById("coordenadasCorte");
                coordenadasCorteInput.value = JSON.stringify(event.detail);

                var canvas = cropper.getCroppedCanvas();
                canvas.toBlob(function (blob) {
                    fotoRecortada = blob;
                });
            }
        });
    });

    var form = document.querySelector("form");
    form.onsubmit = function () {
        if (fotoRecortada) {
            var fotoInput = document.getElementById("imagem");
            fotoInput.files = convertToMultipartFile(fotoRecortada);
        }

        return validarSenhas();
    };
});

function convertToMultipartFile(blob) {
    var fileName = "image.jpg"; // Nome do arquivo desejado

    var formData = new FormData();
    formData.append("file", blob, fileName);

    return formData;
}