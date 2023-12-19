import pandas as pd
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from sklearn.compose import ColumnTransformer
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import confusion_matrix, accuracy_score, classification_report
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from typing import Dict
import joblib

# Load dataset
df = pd.read_csv(
    "/home/tinapyp/Development/CarebyMom/ml_model/app/data/datagenerated.csv", sep=","
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

# Define ColumnTransformer with StandardScaler
numeric_features = X.columns
preprocessor = ColumnTransformer(
    transformers=[("num", StandardScaler(), numeric_features)]
)

# Transform data
X_train_transformed = preprocessor.fit_transform(X_train)
X_test_transformed = preprocessor.transform(X_test)

# Updated Best Hyperparameters
best_hyperparameters = {
    "max_depth": 12,
    "min_samples_leaf": 6,
    "min_samples_split": 10,
    "n_estimators": 50,
}

# Create Random Forest object with updated best hyperparameters and class_weight
random_forest = RandomForestClassifier(
    **best_hyperparameters, class_weight="balanced", random_state=42
)

# Train model
random_forest.fit(X_train_transformed, y_train)

# Save the trained model using joblib
model_filename = (
    "/home/tinapyp/Development/CarebyMom/ml_model/app/model/random_forest_model.joblib"
)
joblib.dump(random_forest, model_filename)
