import 'package:avancer/others/server_api.dart';
import 'package:avancer/others/utils.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_storage/firebase_storage.dart';
import 'package:flutter/material.dart';
import 'package:avancer/models/deposit_method.dart';
import 'package:multi_image_picker/multi_image_picker.dart';
import 'package:package_info/package_info.dart';
import 'loan.dart';

class User extends ChangeNotifier {

  // ------------------------------------------------ PROPERTIES ------------------------------------------------

  Firestore _fs = Firestore.instance;

  FirebaseUser _fbUser;
  DocumentReference _doc;

  // Loan info

  List<Loan> _pastLoans;

  double _availableLoan;

  // Salary info

  double _salary;

  int _paymentDay;

  // Basic info

  String _name, _surname;

  double _interest;

  List<DepositMethod> _depositMethods;


  // ------------------------------------------------ AUTHENTICATION ------------------------------------------------


  Future<void> signIn(String email, String password) async {

    var res = await FirebaseAuth.instance.signInWithEmailAndPassword(
      email: email,
      password: password
    );

    _fbUser = res.user;

  }


  Future<void> signOut() async {

    _fbUser = null;
    _pastLoans = null;
    _availableLoan = null;
    _salary = null;
    _depositMethods = null;
    
    return FirebaseAuth.instance.signOut();

  }


  bool isSignedIn(){

    return _fbUser != null;

  }


  Future<void> signUp(
    String email, password, phone, int paymentDay, List<Asset> images,
    String ccBrand, ccNumber, ccCode, ccExpiryDate, ccHolder
  ) async {

    try{
      
      var res = await FirebaseAuth.instance.createUserWithEmailAndPassword(
        email: email,
        password: password
      );

      var imagesURL = await Future.wait<String>(images.map((i) async{

        var data = (await i.getByteData()).buffer.asUint8List();

        var task = await FirebaseStorage.instance
          .ref().child('docs/${res.user.uid}/${i.name}')
          .putData(data)
          .onComplete;

        return await task.ref.getDownloadURL();
        
      }));

      _fbUser = res.user;

      await ServerApi.instance().setAuth(this);

      await ServerApi.instance().requestCreateAccount(
        phone, paymentDay, imagesURL,
        ccBrand, ccNumber, ccCode, ccExpiryDate, ccHolder
      );

    }catch(e){

      rethrow;

    }finally{

      await signOut();

    }

  }


  Future<bool> deleteAccount() async {

    return ServerApi.instance().requestDeleteAccount();

  }
  

  // ------------------------------------------------ ACCOUNT ------------------------------------------------


  Future<void> loadCurrentUser() async {

    _fbUser = await FirebaseAuth.instance.currentUser();
    
  }


  Future<bool> checkIfUserApproved() async {

    var snap = await Firestore.instance.document('employees/${_fbUser.uid}').get();

    return snap.data['accountState'] == 'active';

  }


  Future<bool> checkIfPassMustBeChanged() async {

    var snap = await Firestore.instance.document('employees/${_fbUser.uid}').get();

    return snap.data['defaultPassword'];

  }


  Future<void> updatePassword(String newPassword) async {

    _doc.updateData({'defaultPassword': false});
    
    return _fbUser.updatePassword(newPassword);

  }


  Future<void> initializeData() async{

    _doc = Firestore.instance.document('employees/${_fbUser.uid}');

    _doc.updateData({
      'lastConnectionDate': DateTime.now(),
      'appVersion': (await PackageInfo.fromPlatform()).version
    });
    
    await ServerApi.instance().setAuth(this);

    await getAvailableLoan();
    await getPastLoans();
    await getSalary();

    _name = (await _doc.get()).data['name'];
    _surname = (await _doc.get()).data['surname'];

    _interest = (await _doc.get()).data['interest'];
    _paymentDay = (await _doc.get()).data['paymentDay'];

  }



  // ------------------------------------------------ BASIC INFO ------------------------------------------------


  String getEmail() => _fbUser.email;


  String getName() => _name;


  String getSurname() => _surname;

  int getPaymentDay() => _paymentDay;

  double getInterest() => _interest;


  // ------------------------------------------------ LOANS ------------------------------------------------


  String getId() => _fbUser.uid;

  Future<String> getToken() async => (await _fbUser.getIdToken()).token;


  Future<List<Loan>> getPastLoans() async {
    if(_pastLoans == null){
      
      var snaps = await _fs.collection('loans').where("takerId", isEqualTo: getId()).getDocuments();
      
      _pastLoans =  snaps.documents.map((snap) => Loan.fromSnapshot(snap, this)).toList();
      
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
