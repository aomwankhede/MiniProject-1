package tech.zeta.application.services;

import tech.zeta.application.models.Vendor;
import tech.zeta.application.repositories.VendorRepository;

import java.util.List;
import java.util.Optional;

public class VendorService {

    private final VendorRepository vendorRepository;
    private static VendorService instance = null;
    private VendorService() {
        this.vendorRepository = new VendorRepository();
    }

    public static VendorService getInstance(){
        if(instance == null){
            instance =  new VendorService();
        }
        return instance;
    }

    // Create new vendor with validation
    public Vendor createVendor(Vendor vendor) {
        if (vendor.getName() == null || vendor.getName().isBlank()) {
            throw new IllegalArgumentException("Vendor name cannot be null or blank");
        }
        if (vendor.getContactEmail() == null || vendor.getContactEmail().isBlank()) {
            throw new IllegalArgumentException("Vendor contact email cannot be null or blank");
        }
        if (vendor.getGstNumber() == null || vendor.getGstNumber().isBlank()) {
            throw new IllegalArgumentException("GST number is required");
        }

        vendorRepository.save(vendor);
        return vendor;
    }

    // Fetch vendor by ID
    public Optional<Vendor> getVendorById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid vendor ID");
        }
        return vendorRepository.findById(id);
    }

    // Fetch all vendors
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    // Update vendor details
    public void updateVendor(Long id,Vendor vendor) {
        if (vendor.getId() == null) {
            throw new IllegalArgumentException("Vendor ID is required for update");
        }

        Optional<Vendor> existing = vendorRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Vendor not found with ID: " + vendor.getId());
        }

        vendorRepository.update(id,vendor);
    }

    // Delete vendor by ID
    public void deleteVendor(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid vendor ID");
        }

        Optional<Vendor> existing = vendorRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Vendor not found with ID: " + id);
        }

        vendorRepository.delete(id);
    }
}
