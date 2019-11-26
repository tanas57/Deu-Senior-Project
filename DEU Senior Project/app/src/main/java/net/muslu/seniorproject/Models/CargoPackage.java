package net.muslu.seniorproject.Models;
/*
{"id":1,"barcode":12345678910,"packageWeigth":3.0,"packageDesi":2.0,"packageOutBranch":null,"packageInBranch":null,
"customer":{"id":1,"fullName":"Tayyip Muslu","address":"Kuruçeşme Mah. Buca/İzmir","phone":"54625554422"},
"packageStatus":null}
 */

import java.util.List;

/**
 * Created by MusluNET on 18.11.19
 */
public class CargoPackage {
    protected int id;
    protected long barcode;
    protected double packageWeigth;
    protected double packageDesi;
    protected Branch packageInBranch;
    protected Branch packageOutBranch;
    protected Customer customer;
    protected List<PackageStatus> packageStatuses;

    public CargoPackage(int id, long barcode, double packageWeigth, double packageDesi, Branch packageInBranch, Branch packageOutBranch, Customer customer, List<PackageStatus> packageStatuses) {
        setId(id);
        setBarcode(barcode);
        setPackageWeigth(packageWeigth);
        setPackageDesi(packageDesi);
        setPackageInBranch(packageInBranch);
        setPackageOutBranch(packageOutBranch);
        setCustomer(customer);
        setPackageStatuses(packageStatuses);
    }

    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public long getBarcode() {
        return barcode;
    }

    protected void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    public double getPackageWeigth() {
        return packageWeigth;
    }

    protected void setPackageWeigth(double packageWeigth) {
        this.packageWeigth = packageWeigth;
    }

    public double getPackageDesi() {
        return packageDesi;
    }

    protected void setPackageDesi(double packageDesi) {
        this.packageDesi = packageDesi;
    }

    public Branch getPackageInBranch() {
        return packageInBranch;
    }

    protected void setPackageInBranch(Branch packageInBranch) {
        this.packageInBranch = packageInBranch;
    }

    public Branch getPackageOutBranch() {
        return packageOutBranch;
    }

    protected void setPackageOutBranch(Branch packageOutBranch) {
        this.packageOutBranch = packageOutBranch;
    }

    public Customer getCustomer() {
        return customer;
    }

    protected void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<PackageStatus> getPackageStatuses() {
        return packageStatuses;
    }

    protected void setPackageStatuses(List<PackageStatus> packageStatuses) {
        this.packageStatuses = packageStatuses;
    }
}
