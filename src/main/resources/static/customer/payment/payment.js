document.addEventListener("DOMContentLoaded", function () {
    const voucherSelect = document.getElementById("voucherSelect");
    const totalTextElement = document.getElementById("totalPrice");
    const submitButton = document.getElementById("submitButton");
    const form = document.querySelector(".payment-form");

    function updateButtonPrice() {
        if (!totalTextElement || !submitButton) return;
        let rawText = totalTextElement.innerText.replace(/[^\d]/g, "");
        let total = parseInt(rawText, 10) || 0;
        const selectedOption = voucherSelect?.options[voucherSelect.selectedIndex];
        const match = selectedOption?.textContent.match(/Giảm\s+(\d+)%/);
        const percent = match ? parseInt(match[1]) : 0;
        const discounted = Math.floor(total * (100 - percent) / 100);
        const formatted = discounted.toLocaleString('vi-VN');
        submitButton.innerText = `Thanh toán ${formatted} VND`;
        const totalPriceInput = form.querySelector("input[name='totalPrice']");
        if (totalPriceInput) {
            totalPriceInput.value = discounted;
        }
    }

    if (voucherSelect) {
        voucherSelect.addEventListener("change", updateButtonPrice);
        updateButtonPrice();
    }
    const bankWrapper = document.querySelector(".bank-wrapper");
    const paymentMethods = document.querySelectorAll("input[name='paymentMethod']");

    function togglePlatformSection() {
        const selectedMethod = document.querySelector("input[name='paymentMethod']:checked");
        bankWrapper?.classList.toggle("show", selectedMethod?.value === "bank");
    }
    togglePlatformSection();
    paymentMethods.forEach(method => {
        method.addEventListener("change", togglePlatformSection);
    });

    if (form) {
        form.addEventListener("submit", function (e) {
            const selectedMethod = document.querySelector("input[name='paymentMethod']:checked")?.value || "cash";
            if (selectedMethod === "cash") {
                form.action = "/booking/showBookingForm/submit";
                let platformInput = form.querySelector("input[name='platform']");
                if (!platformInput) {
                    platformInput = document.createElement("input");
                    platformInput.type = "hidden";
                    platformInput.name = "platform";
                    platformInput.value = "";
                    form.appendChild(platformInput);
                }
            } else {
                form.action = "/booking/showBookingForm/qrpay";
            }
        });
    }
});
