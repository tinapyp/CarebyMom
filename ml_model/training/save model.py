import joblib
import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from imblearn.over_sampling import SMOTE

# Load dataset
# Anda perlu memuat kembali dataset dan melakukan preprocessing sesuai dengan yang dilakukan sebelumnya

# Load dataset
df = pd.read_csv(
    "/home/tinapyp/Development/CarebyMom/ml_model/training/data/datagenerated.csv",
    sep=",",
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

# Handling Imbalanced Data with SMOTE
smote = SMOTE(random_state=42)
X_train_resampled, y_train_resampled = smote.fit_resample(X_train, y_train)

# Train model with the best hyperparameters
best_rf_model = RandomForestClassifier(
    max_depth=15, min_samples_leaf=2, min_samples_split=5, n_estimators=250
)
best_rf_model.fit(X_train_resampled, y_train_resampled)

# Save the model to a file using joblib
model_filename = (
    "/home/tinapyp/Development/CarebyMom/ml_model/app/model/random_forest_model.joblib"
)
joblib.dump(best_rf_model, model_filename)

print(f"Model telah disimpan dalam file: {model_filename}")
