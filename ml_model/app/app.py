import pandas as pd
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from sklearn.compose import ColumnTransformer
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import confusion_matrix, accuracy_score, classification_report
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from typing import Dict

# Load dataset
df = pd.read_csv("data/datagenerated.csv", sep=",")

# Drop duplicated rows
df = df.drop_duplicates()

# Mapping label 'Risk' to numeric values
Risk = {"Normal": 0, "High": 1}
df["Risk"] = df["Risk"].map(Risk).astype(float)

# Split dataset
X = df.drop("Risk", axis=1)
y = df["Risk"]
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.3, random_state=42
)

# Define ColumnTransformer with StandardScaler
numeric_features = X.columns
preprocessor = ColumnTransformer(
    transformers=[("num", StandardScaler(), numeric_features)]
)

# Transform data
X_train_transformed = preprocessor.fit_transform(X_train)
X_test_transformed = preprocessor.transform(X_test)

# Create Random Forest object
random_forest = RandomForestClassifier()

# Train model
random_forest.fit(X_train_transformed, y_train)

# FastAPI Setup
app = FastAPI()


class Item(BaseModel):
    Age: float
    Pregnancy_Duration: float
    Weight_kg: float
    Height_cm: float
    BMI_Score: float
    Arm_Circumference: float
    Fundus_Height: float
    Heart_Rate: float


# Endpoint for prediction
@app.post("/predict")
async def predict(item: Item):
    try:
        input_data = [
            [
                item.Age,
                item.Pregnancy_Duration,
                item.Weight_kg,
                item.Height_cm,
                item.BMI_Score,
                item.Arm_Circumference,
                item.Fundus_Height,
                item.Heart_Rate,
            ]
        ]

        # Transform input data using the same ColumnTransformer
        input_data_transformed = preprocessor.transform(
            pd.DataFrame(input_data, columns=numeric_features)
        )

        prediction = random_forest.predict(input_data_transformed)
        probability = random_forest.predict_proba(input_data_transformed)

        # Map numeric prediction back to label
        prediction_label = {0: "Normal", 1: "High"}
        predicted_risk = prediction_label[prediction[0]]

        # Function to provide advice based on prediction
        def provide_advice(pred):
            if pred == 0:
                return "Risiko kehamilan komplikasi rendah. Lanjutkan perawatan prenatal yang baik."
            elif pred == 1:
                return "Risiko kehamilan komplikasi tinggi. Segera konsultasikan dengan dokter untuk perawatan lebih lanjut."

        advice = provide_advice(int(prediction[0]))

        return {
            "prediction": predicted_risk,
            "probability": float(probability[0][1]),
            "advice": advice,
        }

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Terjadi kesalahan: {str(e)}")


# Endpoint for model evaluation
@app.get("/evaluate")
async def evaluate():
    try:
        train_accuracy = random_forest.score(X_train_transformed, y_train)
        test_accuracy = random_forest.score(X_test_transformed, y_test)

        y_pred = random_forest.predict(X_test_transformed)
        cm = confusion_matrix(y_test, y_pred)
        accuracy = accuracy_score(y_test, y_pred) * 100
        classification_rep = classification_report(
            y_test, random_forest.predict(X_test_transformed), output_dict=True
        )

        return {
            "train_accuracy": train_accuracy,
            "test_accuracy": test_accuracy,
            "confusion_matrix": cm.tolist(),
            "accuracy": accuracy,
            "classification_report": classification_rep,
        }

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Terjadi kesalahan: {str(e)}")
