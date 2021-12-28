package objectProtocol;

import domain.VanzareDTO;

public class VanzareSoldRequest implements UpdateResponse{
    private VanzareDTO VanzareDTO;

    public VanzareSoldRequest(VanzareDTO VanzareDTO) {
        this.VanzareDTO = VanzareDTO;
    }

    public VanzareDTO getVanzareDTO() {
        return VanzareDTO;
    }

    public void setVanzareDTO(VanzareDTO VanzareDTO) {
        this.VanzareDTO = VanzareDTO;
    }
}
