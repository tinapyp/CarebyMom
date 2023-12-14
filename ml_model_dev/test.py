from fastapi import FastAPI
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import confusion_matrix, accuracy_score
from sklearn.metrics import classification_report
from sklearn.ensemble import RandomForestClassifier

app = FastAPI()


@app.get("/random_forest")
def read_root():
    df = pd.read_csv(
        "/home/tinapyp/Development/CarebyMom/ml_model_dev/data/datagenerated.csv",
        sep=",",
    )
    data_dup = df.duplicated().any()
    df = df.drop_duplicates()

    ca_val = []
    co_val = []

    for column in df.columns:
        if df[column].nunique() <= 10:
            ca_val.append(column)
        else:
            co_val.append(column)

    Risk = {
        "Normal": 0,
        "High": 1,
    }

    df["Risk"] = df["Risk"].map(Risk).astype(float)
    X = df.drop("Risk", axis=1)
    y = df["Risk"]
    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.3, random_state=42
    )

    ss = StandardScaler()
    X_train = ss.fit_transform(X_train)
    X_test = ss.transform(X_test)
    random_forest = RandomForestClassifier()
    random_forest.fit(X_train, y_train)

    train_accuracy = random_forest.score(X_train, y_train)
    test_accuracy = random_forest.score(X_test, y_test)

    y_pred = random_forest.predict(X_test)
    cm = confusion_matrix(y_test, y_pred)
    accuracy = accuracy_score(y_test, y_pred) * 100
    classificationReport = classification_report(y_test, random_forest.predict(X_test))

    return {
        "train_accuracy": train_accuracy,
        "test_accuracy": test_accuracy,
        "y_pred": y_pred.tolist(),
        "confusion_matrix": cm.tolist(),
        "accuracy": accuracy,
        "classification_report": classificationReport,
    }
