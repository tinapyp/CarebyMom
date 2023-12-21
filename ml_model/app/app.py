import pandas as pd
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from sklearn.compose import ColumnTransformer
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
import joblib
from typing import List

# Load the saved model
model_filename = "model/random_forest_model.joblib"
random_forest = joblib.load(model_filename)

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

        # Make predictions
        prediction = random_forest.predict(input_data)
        probability = random_forest.predict_proba(input_data)

        # Map numeric prediction back to label
        prediction_label = {0: "Normal", 1: "High"}
        predicted_risk = prediction_label[prediction[0]]

        # Function to provide advice based on prediction
        def provide_advice(pred):
            if pred == 0:
                return "The risk of pregnancy complications is low. Continue good prenatal care."
            elif pred == 1:
                return "The risk of pregnancy complications is high. Immediately consult a doctor for further treatment."

        advice = provide_advice(int(prediction[0]))

        return {
            "prediction": predicted_risk,
            "probability": float(probability[0][1]),
            "advice": advice,
        }

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"An error occurred: {str(e)}")
