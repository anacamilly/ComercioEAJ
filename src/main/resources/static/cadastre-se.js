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

 function maskPhone(input) {
     // Remove todos os caracteres que não são dígitos
     var phoneNumber = input.value.replace(/\D/g, '');

     // Remove espaços, o sinal de adição (+), parênteses e hífen
     phoneNumber = phoneNumber.replace(/[+\s()-]/g, '');

     // Define o formato da máscara
     var phoneNumberMask = '+__ (__) _____-____';

     // Itera sobre os caracteres da máscara e substitui os "_" pelos dígitos do número de telefone
     var maskedPhoneNumber = '';
     var j = 0;
     for (var i = 0; i < phoneNumberMask.length; i++) {
         if (phoneNumberMask.charAt(i) === '_') {
             maskedPhoneNumber += phoneNumber.charAt(j) || '_';
             j++;
         } else {
             maskedPhoneNumber += phoneNumberMask.charAt(i);
         }
     }

     // Atualiza o valor do campo de entrada com a máscara aplicada
     input.value = maskedPhoneNumber;
 }
