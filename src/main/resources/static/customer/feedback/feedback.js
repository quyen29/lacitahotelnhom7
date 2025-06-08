function setupStarRating(groupId, inputId) {
    const stars = document.querySelectorAll(`#${groupId} i`);
    const ratingInput = document.getElementById(inputId);
    let selectedRating = 0;

    stars.forEach(star => {
        star.addEventListener('mouseenter', () => {
            const value = parseInt(star.dataset.value);
            stars.forEach(s => {
                s.classList.toggle('hovered', parseInt(s.dataset.value) <= value);
            });
        });

        star.addEventListener('mouseleave', () => {
            stars.forEach(s => s.classList.remove('hovered'));
        });

        star.addEventListener('click', () => {
            selectedRating = parseInt(star.dataset.value);
            ratingInput.value = selectedRating;
            stars.forEach(s => {
                s.classList.toggle('selected', parseInt(s.dataset.value) <= selectedRating);
            });
        });
    });
}

document.addEventListener('DOMContentLoaded', function () {
    setupStarRating("roomRatingGroup", "roomRating");
    setupStarRating("serviceRatingGroup", "serviceRating");
});

