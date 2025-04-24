document.addEventListener("DOMContentLoaded", function () {
    const checkInDate = document.getElementById("checkInDate");
    const checkOutDate = document.getElementById("checkOutDate");
    const checkInHour = document.getElementById("checkInHour");
    const checkOutHour = document.getElementById("checkOutHour");
    const roomQuantity = document.getElementById("roomQuantitySelect");
    const priceDisplay = document.getElementById("priceDisplay");
    const pricePerRoom = parseFloat(document.getElementById("priceValue").dataset.price) || 0;
    const peopleSelect = document.getElementById("totalPeopleSelect");
    const maxOccupancy = parseInt(document.getElementById("maxPeople").innerText) || 1;

    function calculatePrice() {
        const inDate = checkInDate.value;
        const outDate = checkOutDate.value;
        const inHour = parseInt(checkInHour.value);
        const outHour = parseInt(checkOutHour.value);
        const quantity = parseInt(roomQuantity.value);

        if (inDate && outDate && !isNaN(inHour) && !isNaN(outHour) && !isNaN(quantity)) {
            const start = new Date(inDate);
            start.setHours(inHour);

            const end = new Date(outDate);
            end.setHours(outHour);

            const diffMs = end - start;
            const diffHours = diffMs / (1000 * 60 * 60);

            if (diffHours <= 0) {
                priceDisplay.innerText = "Tổng giá: 0 VND (0 giờ, 0 phòng)";
                document.getElementById("hiddenTotalPrice").value = 0;
                return;
            }

            const slots = Math.ceil(diffHours / 12);
            const total = pricePerRoom * slots * quantity;

            priceDisplay.innerText = `Tổng giá: ${total.toLocaleString()} VND (${Math.floor(diffHours)} giờ, ${quantity} phòng)`;

            document.getElementById("hiddenTotalPrice").value = total;
        }
    }

    function updatePeopleOptions() {
        const quantity = parseInt(roomQuantity.value);

        if (!isNaN(quantity)) {
            const min = quantity;
            const max = quantity * maxOccupancy;

            peopleSelect.innerHTML = "";
            for (let i = min; i <= max; i++) {
                const option = document.createElement("option");
                option.value = i;
                option.textContent = i;
                peopleSelect.appendChild(option);
            }
            peopleSelect.value = min;
        }
    }

    [checkInDate, checkOutDate, checkInHour, checkOutHour, roomQuantity].forEach(input => {
        input.addEventListener("change", () => {
            calculatePrice();
            updatePeopleOptions();
        });
    });

    updatePeopleOptions();
    calculatePrice();

    document.getElementById("bookingSubmitForm").addEventListener("submit", function () {
        document.getElementById("hiddenCheckInDate").value = checkInDate.value;
        document.getElementById("hiddenCheckOutDate").value = checkOutDate.value;
        document.getElementById("hiddenCheckInHour").value = checkInHour.value;
        document.getElementById("hiddenCheckOutHour").value = checkOutHour.value;
        document.getElementById("hiddenRoomQuantity").value = roomQuantity.value;
        document.getElementById("hiddenTotalPeople").value = peopleSelect.value;
        document.getElementById("hiddenPaymentMethod").value = document.getElementById("paymentMethodSelect").value;
    });
});