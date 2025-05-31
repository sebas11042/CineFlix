const dropZone = document.getElementById("dropZone");
const fileInput = document.getElementById("imagenInput");
const previewImage = document.getElementById("previewImage");
const imagenUrlHidden = document.getElementById("imagenUrl");

dropZone.addEventListener("click", () => fileInput.click());

dropZone.addEventListener("dragover", (e) => {
  e.preventDefault();
  dropZone.classList.add("bg-light");
});

dropZone.addEventListener("dragleave", () => {
  dropZone.classList.remove("bg-light");
});

dropZone.addEventListener("drop", (e) => {
  e.preventDefault();
  dropZone.classList.remove("bg-light");

  const file = e.dataTransfer.files[0];
  if (file && file.type.startsWith("image/")) {
    fileInput.files = e.dataTransfer.files;
    mostrarPreview(file);
  }
});

fileInput.addEventListener("change", (e) => {
  const file = e.target.files[0];
  if (file) {
    mostrarPreview(file);
  }
});

function mostrarPreview(file) {
  const reader = new FileReader();
  reader.onload = (e) => {
    previewImage.src = e.target.result;
    imagenUrlHidden.value = e.target.result; // opcional para usar como string base64
  };
  reader.readAsDataURL(file);
}
