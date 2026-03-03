from flask import Flask, request, jsonify
import random
import requests

app = Flask(__name__)

FIREBASE_URL = "https://randompickerserver-default-rtdb.firebaseio.com/"

def get_date_din_firebase():
    response = requests.get(FIREBASE_URL + ".json")
    if response.status_code == 200 and response.json() is not None:
        return response.json()
    return {}

@app.route('/extrage', methods=['GET'])
def extrage_random():
    categorie = request.args.get('categorie')
    
    baza_de_date = get_date_din_firebase()
    
    if categorie in baza_de_date and len(baza_de_date[categorie]) > 0:
        elemente = baza_de_date[categorie]
        
        if isinstance(elemente, dict):
            elemente = list(elemente.values())
            
        castigator = random.choice(elemente)
        return str(castigator), 200
    else:
        return "Categoria nu exista sau este goala!", 404

@app.route('/adauga', methods=['POST'])
def adauga_idee():
    try:
        date = request.json
        categorie = date.get('categorie')
        idee_noua = date.get('idee')
        
        url_categorie = f"{FIREBASE_URL}{categorie}.json"
        raspuns = requests.post(url_categorie, json=idee_noua)
        
        if raspuns.status_code == 200:
            print(f"--> S-a salvat in CLOUD: '{idee_noua}' la '{categorie}'.")
            return "Idee adaugata cu succes in Cloud!", 200
        else:
            return "Eroare la salvarea in cloud", 400
    except Exception as e:
        return f"Eroare la procesare: {str(e)}", 500

def initializare_baza_de_date():
    if not get_date_din_firebase():
        print("Baza de date e goala! O populam cu valorile default...")
        date_default = {
            "Mancare": ["Pizza", "Sushi", "Burger", "Shaorma"],
            "Filme": ["Comedie", "Horror", "Actiune", "Sci-Fi"],
            "Activitati": ["Plimbare in parc", "Boardgames", "Citit"]
        }
        requests.put(FIREBASE_URL + ".json", json=date_default)

if __name__ == '__main__':
    initializare_baza_de_date()
    app.run(host='0.0.0.0', port=5000)