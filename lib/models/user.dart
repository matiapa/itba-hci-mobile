import 'package:avancer/others/server_api.dart';
import 'package:avancer/others/utils.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:avancer/models/deposit_method.dart';
import 'package:package_info/package_info.dart';
import 'loan.dart';

class User extends ChangeNotifier {
  // ------------------------------------------------ PROPERTIES ------------------------------------------------

  Firestore _fs = Firestore.instance;

  FirebaseUser _fbUser;
  DocumentReference _doc;

  List<Loan> _pastLoans;

  double _availableLoan;

  double _salary;

  String _name, _surname;

  List<DepositMethod> _depositMethods;

  
  // ------------------------------------------------ ACCOUNT ------------------------------------------------

  Future<void> initializeData() async{
    
    if(! isSignedIn()){
      return;
    }

    _doc = Firestore.instance.document('employees/${_fbUser.uid}');

    _doc.updateData({
      'lastConnectionDate': DateTime.now(),
      'appVersion': (await PackageInfo.fromPlatform()).version
    });

    await setUpServerAuth();

    await getAvailableLoan();
    await getPastLoans();
    await getSalary();

    _name = (await _doc.get()).data['name'];
    _surname = (await _doc.get()).data['surname'];

  }


  // ------------------------------------------------ ACCOUNT ------------------------------------------------


  Future<void> loadFirebaseUser() async {
    _fbUser = await FirebaseAuth.instance.currentUser();
  }


  Future<void> setUpServerAuth() async {
    ServerApi.instance().setAuth(_fbUser.uid, (await _fbUser.getIdToken()).token);
  }


  bool isSignedIn(){
    return _fbUser != null;
  }


  Future<void> signIn(String email, String password) async {

    var res = await FirebaseAuth.instance.signInWithEmailAndPassword(email: email, password: password);

    _fbUser = res.user;

    await initializeData();

  }


  Future<void> signOut() async {
    _fbUser = null;
    _pastLoans = null;
    _availableLoan = null;
    _salary = null;
    _depositMethods = null;
    return FirebaseAuth.instance.signOut();
  }


  /* -------------------------------------------------------------------
    Deletes user account, it can't be called if user has debt
  ------------------------------------------------------------------- */

  Future<bool> deleteAccount() async {
    return ServerApi.instance().deleteAccount();
  }


  Future<bool> checkIfPassMustBeChanged() async {
    var snap = await _doc.get();
    return snap.data['defaultPassword'];
  }


  Future<void> updatePassword(String newPassword) async {
    _doc.updateData({'defaultPassword': false});
    return _fbUser.updatePassword(newPassword);
  }


  String email(){
    return _fbUser.email;
  }


  String getName() => _name;


  String getSurname() => _surname;


  // ------------------------------------------------ LOANS ------------------------------------------------


  String getId() => _fbUser.uid;


  Future<List<Loan>> getPastLoans() async {
    if(_pastLoans == null){
      
      var snaps = await _fs.collection('loans').where("takerId", isEqualTo: getId()).getDocuments();
      
      _pastLoans =  snaps.documents.map((snap) => Loan.fromSnapshot(snap)).toList();
      
    }

    return _pastLoans;
  }


  Future<double> getAvailableLoan() async {
    if(_availableLoan == null){
      _availableLoan = await ServerApi.instance().getAvailableLoan();
    }

    return _availableLoan;
  }


  Future<double> getSalary() async {
    if(_salary == null){
      _salary = (await _doc.get()).data['salary'] + 0.0;
    }

    return _salary;
  }


  Future<bool> requestLoan(double amount, DepositMethod depositMethod) async{

    try{

      Loan newLoan = await Loan.request(amount, depositMethod);

      _pastLoans.add(newLoan);
      _availableLoan -= newLoan.getAmount();
    
      notifyListeners();
      return true;

    }catch(error){

      return false;
    
    }

  }


  // ------------------------------------------------ DEPOSIT METHODS ------------------------------------------------


  Future<List<DepositMethod>> getDepositMethods() async {
    if(_depositMethods == null){
      
      var snaps = await _fs.collection('depositMethods').where("ownerId", isEqualTo: getId()).getDocuments();
      
      _depositMethods = snaps.documents.map((snap){

        if(snap.data['type'] == Utils.enumToStr(DepositMethodType.BANK)){
          return BankAccount.fromSnapshot(snap);
        }else{
          return MercadoPagoAccount.fromSnapshot(snap);
        }

      }).toList();
      
    }

    return _depositMethods;
  }


  Future<void> addDepositMethod(DepositMethod newMethod) async{

    await newMethod.post(getId());

    _depositMethods.add(newMethod);
    notifyListeners();
    
  }


}
