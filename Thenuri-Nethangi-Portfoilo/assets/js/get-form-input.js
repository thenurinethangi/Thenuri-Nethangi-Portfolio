let submitBtn = document.querySelector('#form-btn>button');

submitBtn.addEventListener('click',getInputData);

function getInputData(event){

    event.preventDefault();

    let form = submitBtn.parentElement.parentElement;

    let inputs = form.children;
    for (let i = 0; i < inputs.length-1; i++) {
        console.log(inputs[i].value);
        inputs[i].value = '';
    }
}

