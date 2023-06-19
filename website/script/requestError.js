fetch("https://www.data.gouv.fr/fr/datasets/r/5fb6d2e3-609c-481d-9104-350e9ca134fa")
  .then(() => console.log("Should never go here"))
  .catch(error => {
    const content = document.querySelector("#queryContent");
    content.innerHTML = `
      <p>Sans proxy, une erreur a lieu lors de la requête. Vérifiez dans la console →</p>
      ${error}
    `;
    content.classList.remove("blink", "info-text");
    content.classList.add("error-text");
    console.log(error)
  })