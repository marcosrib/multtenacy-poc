package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
public class CouponController {

    private final CouponRepository repository;

    public CouponController(CouponRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/cupon")
    public List<Coupon> getCoupons() {

        return repository.findAll();
    }

    @PostMapping("/cupon")
    public Coupon create(@RequestBody Coupon coupon) {
        System.out.println(coupon);
        try {
            new GereteCoupon().excecute(new Comprovate("marcos", "508808805986"), "logo.jpeg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
        //return repository.save(coupon);
    }
}
