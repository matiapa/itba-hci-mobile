import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/foundation.dart';

enum DepositMethodType{BANK, MERCADOPAGO}

abstract class DepositMethod{

  String _id;
  String _name;

  DepositMethod({String id, @required String name}){
    this._id = id;
    this._name = name;
  }

  Future<void> post(String ownerId);

  String getId() => _id;

  String getName() => _name;

  String getDescription();

}


class MercadoPagoAccount extends DepositMethod{

  String _email;

  MercadoPagoAccount({@required String email, @required String name, String id})
   : _email = email
   , super(id: id, name: name);


  static MercadoPagoAccount fromSnapshot(DocumentSnapshot snap){
    return MercadoPagoAccount(
      id: snap.documentID,
      name: snap.data['name'],
      email: snap.data['email'],
    );
  }


  @override
  Future<void> post(String ownerId) async {
    
    var docRef = await Firestore.instance.collection('depositMethods').add({
      'ownerId': ownerId,
      'type': 'MERCADOPAGO',
      'name': _name,
      'email': _email,
    });

    _id = docRef.documentID;

  }


  String getUsername() => _email;

  @override
  String getDescription() => 'Email '+_email;

}


class BankAccount extends DepositMethod{

  String _cbu;
  String _cuit;

  BankAccount({@required String cbu, @required String cuit, @required String name, String id}):
    this._cbu = cbu,
    this._cuit = cuit,
    super(id: id, name: name);


  static BankAccount fromSnapshot(DocumentSnapshot snap){
    return BankAccount(
      id: snap.documentID,
      name: snap.data['name'],
      cbu: snap.data['cbu'],
      cuit: snap.data['cuit']
    );
  }


  @override
  Future<void> post(String ownerId) async {
    
    var docRef = await Firestore.instance.collection('depositMethods').add({
      'ownerId': ownerId,
      'type': 'BANK',
      'name': _name,
      'cbu': _cbu,
      'cuit': _cuit
    });

    _id = docRef.documentID;

  }


  String getCbu() => _cbu;

  String getCuit() => _cuit;

   @override
  String getDescription() => 'CBU '+_cbu;

}