document.addEventListener("DOMContentLoaded", function () {
    const checkinInput = document.getElementById("checkinImage");
    const checkoutInput = document.getElementById("checkoutImage");

    checkinInput.addEventListener("change", function (event) {
        previewImage(event, "checkinPreview", "checkinFilename");
    });

    checkoutInput.addEventListener("change", function (event) {
        previewImage(event, "checkoutPreview", "checkoutFilename");
    });

    function previewImage(event, imageId, filenameId) {
        const file = event.target.files[0];
        const imageElement = document.getElementById(imageId);
        const filenameElement = document.getElementById(filenameId);

        if (file) {
            imageElement.src = URL.createObjectURL(file);
            imageElement.style.display = "block";
            filenameElement.textContent = file.name;
        } else {
            imageElement.src = "";
            imageElement.style.display = "none";
            filenameElement.textContent = "Chưa chọn ảnh";
        }
    }
});