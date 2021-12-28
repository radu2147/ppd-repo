package domain.validators;

import domain.Vanzare;

public class VanzareValidator implements Validator<Vanzare> {
    @Override
    public void validate(Vanzare entity) throws ValidationException {
        if(entity.getFestival()==null)
            throw new ValidationException("Festivalul pentru care se vinde biletul nu poate fi null!");
    }
}
