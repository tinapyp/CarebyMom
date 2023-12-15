Berikut adalah dokumentasi dalam format Markdown untuk FastAPI:

### Endpoint untuk Prediksi

**URL**: `/predict`  
**Metode**: `POST`  
**Deskripsi**: Endpoint ini digunakan untuk melakukan prediksi berdasarkan fitur yang diberikan.

**Request Body:**

```json
{
  "Age": 25.0,
  "Pregnancy_Duration": 30.0,
  "Weight_kg": 60.0,
  "Height_cm": 165.0,
  "BMI_Score": 22.0,
  "Arm_Circumference": 28.0,
  "Fundus_Height": 20.0,
  "Heart_Rate": 80.0
}
```

**Response:**

```json
{
  "prediction": string,
  "probability": float,
  "advice": "string"
}
```

### Endpoint untuk Evaluasi Model

**URL**: `/evaluate`  
**Metode**: `GET`  
**Deskripsi**: Endpoint ini digunakan untuk mengevaluasi performa model yang telah di-train.

**Response:**

```json
{
  "train_accuracy": float,
  "test_accuracy": float,
  "confusion_matrix": [[int, int], [int, int]],
  "accuracy": float,
  "classification_report": {
    "0": {
      "precision": float,
      "recall": float,
      "f1-score": float,
      "support": int
    },
    "1": {
      "precision": float,
      "recall": float,
      "f1-score": float,
      "support": int
    },
    "accuracy": float,
    "macro avg": {
      "precision": float,
      "recall": float,
      "f1-score": float,
      "support": int
    },
    "weighted avg": {
      "precision": float,
      "recall": float,
      "f1-score": float,
      "support": int
    }
  }
}
```

### Deployment dengan Docker

Untuk mendeploy aplikasi FastAPI ini menggunakan Docker, ikuti langkah-langkah berikut:

1. Pastikan Docker telah terinstal di sistem Anda.

2. Buat file `Dockerfile` di direktori yang sama dengan aplikasi FastAPI Anda:

   ```dockerfile
   FROM tiangolo/uvicorn-gunicorn-fastapi:python3.8

   WORKDIR /app

   COPY ./app /app

   RUN pip install -r requirements.txt

   CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "8000"]
   ```

3. Jalankan perintah berikut di terminal untuk membangun Docker image:

   ```bash
   docker build -t nama_image .
   ```

4. Setelah berhasil dibangun, jalankan container dengan perintah:

   ```bash
   docker run -p 8000:8000 nama_image
   ```

Aplikasi FastAPI sekarang dapat diakses melalui `http://localhost:8000`. Pastikan untuk mengganti `nama_image` dengan nama yang sesuai untuk Docker image Anda.
