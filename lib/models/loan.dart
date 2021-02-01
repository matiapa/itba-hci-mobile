import 'dart:async';
import 'package:avancer/models/user.dart';
import 'package:avancer/others/server_api.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:avancer/models/deposit_method.dart';
import 'package:flutter/widgets.dart';

enum LoanState{REQUESTED, GRANTED, REPAID}

class Loan implements Comparable{

  // ------------------------------------------------ PROPERTIES ------------------------------------------------

  User _user;

  String _loanId;

  double _amount;
  LoanState _state;

  DateTime _requestDate;
  DateTime _limitDate;
  

  // ------------------------------------------------ BUILDERS ------------------------------------------------


  Loan({
    @required User user,
    String loanId,
    double amount,
    DateTime requestDate,
    DateTime dueDate,
    LoanState state,
  }){
    this._user = user;
    this._loanId = loanId;

    this._amount = amount;

    this._requestDate = requestDate;
    this._limitDate = dueDate;

    this._state = state ?? LoanState.REQUESTED;
  }


  static Loan fromSnapshot(DocumentSnapshot snap, User user){
    return new Loan(
      user: user,
      loanId: snap.documentID,
      amount: snap.data['amount'] + 0.0,
      requestDate: snap.data['requestDate'].toDate(),
      dueDate: snap.data['dueDate'].toDate(),
      state: strToLoanState(snap.data['state']),
    );
  }


  // ------------------------------------------------ METHODS ------------------------------------------------


  @override
  int compareTo(other) {

    if(!(other is Loan))
      throw ArgumentError("Can't compare with non Loan object");

    return (other as Loan)._requestDate.compareTo(_requestDate);

  }

  static Future<Loan> request(double amount, DepositMethod depositMethod) async{
    return await ServerApi.instance().requestLoan(amount, depositMethod);
  }


  // ------------------------------------------------ GETTERS ------------------------------------------------


  String getLoanId() => _loanId;
  

  LoanState getState() => _state;


  DateTime getRequestDate() => _requestDate;


  double getAmount() => _amount;


  Future<void> fetchLimitDate() async{
    _limitDate = await ServerApi.instance().getLimitDate();
  }

  DateTime getLimitDate() {
    return _limitDate;
  }


  double _getDueOnDate(DateTime date){

    var creditDays = date.difference(_requestDate).inDays + 1;

    return _amount * (1 + _user.getInterest() * creditDays / 30.5);

  }

  double getDueToday() {

    return _getDueOnDate(DateTime.now());

  }

  double getDueOnLimit() {

    return _getDueOnDate(getLimitDate());

  }



  // ------------------------------------------------ OTHERS ------------------------------------------------

  static LoanState strToLoanState(String str) => LoanState.values.firstWhere((value) => value.toString() == 'LoanState.'+str.toUpperCase());

}