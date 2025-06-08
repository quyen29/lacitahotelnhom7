from flask import Flask, request, jsonify
from flask_cors import CORS
from deepface import DeepFace
import io
import numpy as np
from PIL import Image
import pymysql

app = Flask(__name__)
CORS(app)

def get_checkin_image_from_db(room_id, booking_id):
    connection = pymysql.connect(
        host="localhost",
        user=YOUR_USERNAME,
        password=YOUR_PASSWORD,
        database="bookingsystemdb",
        cursorclass=pymysql.cursors.DictCursor
    )
    try:
        with connection.cursor() as cursor:
            sql = """
                SELECT b.check_in_image FROM booking b
                JOIN booking_room br ON b.booking_id = br.booking_id
                WHERE br.room_id = %s AND br.booking_id = %s
            """
            cursor.execute(sql, (room_id, booking_id))
            result = cursor.fetchone()
            if result and result['check_in_image']:
                return result['check_in_image']
    finally:
        connection.close()
    return None

def calculate_similarity(distance):
    similarity = max(0.0, min(100.0, round((1 - distance / 2) * 100, 2)))
    return similarity

@app.route("/verify", methods=["POST"])
def verify():
    try:
        img1 = request.files["image1"]
        img2 = request.files["image2"]
        img1_np = np.array(Image.open(io.BytesIO(img1.read())).convert("RGB"))
        img2_np = np.array(Image.open(io.BytesIO(img2.read())).convert("RGB"))
        result = DeepFace.verify(img1_np, img2_np, model_name="ArcFace")
        similarity = calculate_similarity(result["distance"])
        return jsonify({
            "verified": result["verified"],
            "distance": result["distance"],
            "similarity": similarity
        })
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route("/verify-use-checkin-from-db", methods=["POST"])
def verify_with_checkin_from_db():
    try:
        room_id = request.form.get("roomID")
        booking_id = request.form.get("bookingID")
        image2 = request.files.get("image2")
        if not room_id or not booking_id or not image2:
            return jsonify({"error": "Thiếu dữ liệu"}), 400
        checkin_blob = get_checkin_image_from_db(room_id, booking_id)
        if not checkin_blob:
            return jsonify({"error": "Không tìm thấy check_in_image trong cơ sở dữ liệu"}), 404
        img1_np = np.array(Image.open(io.BytesIO(checkin_blob)).convert("RGB"))
        img2_np = np.array(Image.open(io.BytesIO(image2.read())).convert("RGB"))
        result = DeepFace.verify(img1_np, img2_np, model_name="ArcFace")
        similarity = calculate_similarity(result["distance"])
        return jsonify({
            "verified": result["verified"],
            "distance": result["distance"],
            "similarity": similarity
        })
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="127.0.0.1", port=5000, debug=True)