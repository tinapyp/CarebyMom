import pandas as pd
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from typing import List

# Load dataset
df = pd.read_csv(
    "/home/tinapyp/Development/CarebyMom/ml_model_dev/data/datagenerated.csv", sep=","
)

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

# Standardize data
ss = StandardScaler()
X_train = ss.fit_transform(X_train)
X_test = ss.transform(X_test)

# Create Random Forest object
random_forest = RandomForestClassifier()

# Train model
random_forest.fit(X_train, y_train)

# FastAPI Setup
app = FastAPI()


class Item(BaseModel):
    features: List[float]


# Endpoint for prediction
@app.post("/predict")
async def predict(item: Item):
    features = item.features
    input_data = [features]  # Data should be in the form of a list of lists
    input_data = ss.transform(input_data)  # Standardize input data

    prediction = random_forest.predict(input_data)
    probability = random_forest.predict_proba(input_data)

    return {"prediction": int(prediction[0]), "probability": float(probability[0][1])}


# Endpoint for model evaluation
@app.get("/evaluate")
async def evaluate():
    train_accuracy = random_forest.score(X_train, y_train)
    test_accuracy = random_forest.score(X_test, y_test)

    y_pred = random_forest.predict(X_test)
    cm = confusion_matrix(y_test, y_pred)
    accuracy = accuracy_score(y_test, y_pred) * 100
    classification_rep = classification_report(
        y_test, random_forest.predict(X_test), output_dict=True
    )

    return {
        "train_accuracy": train_accuracy,
        "test_accuracy": test_accuracy,
        "confusion_matrix": cm.tolist(),
        "accuracy": accuracy,
        "classification_report": classification_rep,
    }
